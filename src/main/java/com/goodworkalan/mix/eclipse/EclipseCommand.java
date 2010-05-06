package com.goodworkalan.mix.eclipse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.library.Artifact;
import com.goodworkalan.go.go.library.ArtifactPart;
import com.goodworkalan.go.go.library.PathPart;
import com.goodworkalan.go.go.library.PathParts;
import com.goodworkalan.mix.Dependency;
import com.goodworkalan.mix.Mix;
import com.goodworkalan.mix.MixCommand;
import com.goodworkalan.mix.MixError;
import com.goodworkalan.mix.MixException;
import com.goodworkalan.mix.Project;
import com.goodworkalan.mix.Recipe;

@Command(parent = MixCommand.class)
public class EclipseCommand implements Commandable {
    public void execute(Environment env) {
        Mix mix = env.get(Mix.class, 0);
        env.verbose("start", env.commands, mix.getWorkingDirectory());

        File file = new File(mix.getWorkingDirectory(), ".classpath");
        if (!file.exists()) {
            throw new MixError(EclipseCommand.class, "classpath.missing", file);
        }

        DocumentBuilder db;
        try {
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // Yeah, well, we never change the default configuration, so WTF?
            throw new RuntimeException(e);
        }
        Document doc;
        try {
            doc = db.parse(file);
        } catch (SAXException e) {
            throw new MixError(EclipseCommand.class, "classpath.parse", file, e.getMessage());
        } catch (IOException e) {
            throw new MixError(EclipseCommand.class, "classpath.io", file, e.getMessage());
        }

        
        NodeList nodeList = doc.getElementsByTagName("classpathentry");
        int i = 0, stop = nodeList.getLength();
        while (i < stop) {
            Element element = (Element) nodeList.item(i);
            env.debug("part", i, stop, element.getAttribute("kind")); 
            if (element.getAttribute("kind").equals("var")) {
                element.getParentNode().removeChild(element);
                stop--;
            } else {
                i++;
            }
        }
        
        Project project = env.get(Project.class, 0);
        
        List<PathPart> parts = new ArrayList<PathPart>();
        for (Recipe recipe : project.getRecipes()) {
            for (Dependency dependency : recipe.getDependencies()) {
                parts.addAll(dependency.getPathParts(project));
            }
        }
        
        List<Artifact> artifacts = PathParts.artifactsList(env.library.resolve(parts));
        
        Map<File, String> variables = new HashMap<File, String>();
        if (System.getProperty("user.home") != null) {
            File home = new File(System.getProperty("user.home"));
            if (home.exists()) {
                File homeRepository = new File(home, ".m2/repository");
                if (homeRepository.isDirectory()) {
                    variables.put(homeRepository, "M2_REPO");
                }
            }
        }
        
        int count = 1;
        for (Artifact artifact : artifacts) {
            ArtifactPart artifactPart = env.library.getArtifactPart(artifact);
            if (artifactPart == null) {
                throw new MixError(EclipseCommand.class, "artifact.missing", artifact);
            }
            if (!variables.containsKey(artifactPart.getLibraryDirectory())) {
                variables.put(artifactPart.getLibraryDirectory(), "REPO_" + count);
                count++;
            }
        }

        Element classpath = doc.getDocumentElement();
        DocumentFragment fragment = doc.createDocumentFragment();
        fragment.appendChild(doc.createTextNode("\n  "));
        Element entry = doc.createElement("classpathentry");
        entry.setAttribute("kind", "var");
        for (Artifact artifact : artifacts) {
            env.debug("add", artifact);
            ArtifactPart artifactPart = env.library.getArtifactPart(artifact);
            File directory = artifactPart.getLibraryDirectory();
            File jar = new File(directory, artifact.getPath("jar"));
            if (!jar.exists()) {
                throw new MixError(EclipseCommand.class, "jar.missing", artifact);
            }
            String variable = variables.get(directory);
            Element part = doc.createElement("classpathentry");
            part.setAttribute("kind", "var");
            part.setAttribute("path", variable + "/" + artifact.getPath("jar"));
            File source = new File(directory, artifact.getPath("sources/jar"));
            if (source.exists()) {
                part.setAttribute("sourcepath", variable + "/" + artifact.getPath("sources/jar"));
            }
            File javadoc = new File(directory, artifact.getPath("javadoc/jar"));
            if (javadoc.exists()) {
                Element attributes = doc.createElement("attributes");
                Element attribute = doc.createElement("attribute");
                attribute.setAttribute("value", "jar:" + javadoc.toURI().toASCIIString() + "!/");
                attribute.setAttribute("name", "javadoc_location");
                attributes.appendChild(attribute);
                part.appendChild(attributes);
            }
            classpath.appendChild(part);
        }
        
        Node node = classpath.getFirstChild();
        while (node != null) {
            Node next = node.getNextSibling();
            if (node.getNodeType() == Node.TEXT_NODE) {
                node.getParentNode().removeChild(node);
            }
            node = next;
        }
        
        try {
            TransformerFactory factory = TransformerFactory.newInstance();

            factory.setAttribute("indent-number", new Integer(2));

            Transformer xformer = factory.newTransformer();

            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            xformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
     
            Source source = new DOMSource(doc);
            Result result = new StreamResult(new FileWriter(file));
            xformer.transform(source, result);
        } catch (TransformerException e) {
            throw new MixException(EclipseCommand.class, "classpath.pretty", e, file, e.getMessageAndLocation());
        } catch (IOException e) {
            throw new MixException(EclipseCommand.class, "classpath.write", e, file, e.getMessage());
        }
    }
}
