package com.goodworkalan.mix.eclipse;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodworkalan.comfort.io.Files;
import com.goodworkalan.comfort.xml.Document;
import com.goodworkalan.comfort.xml.Element;
import com.goodworkalan.comfort.xml.Serializer;
import com.goodworkalan.go.go.Argument;
import com.goodworkalan.go.go.Command;
import com.goodworkalan.go.go.Commandable;
import com.goodworkalan.go.go.Environment;
import com.goodworkalan.go.go.library.Artifact;
import com.goodworkalan.go.go.library.ArtifactPart;
import com.goodworkalan.go.go.library.PathPart;
import com.goodworkalan.go.go.library.PathParts;
import com.goodworkalan.mix.CafeCommand;
import com.goodworkalan.mix.Dependency;
import com.goodworkalan.mix.Mix;
import com.goodworkalan.mix.MixError;
import com.goodworkalan.mix.Project;
import com.goodworkalan.mix.Recipe;

// TODO Document.
@Command(parent = CafeCommand.class)
public class EclipseCommand implements Commandable {
    // TODO Document.
    @Argument
    public boolean includeMix;
    
    // TODO Document.
    @Argument
    public String description;

    // TODO Document.
    private String join(String...parts) {
        StringBuilder string = new StringBuilder();
        String separator = "";
        for (String part : parts) {
            string.append(separator).append(part);
            separator = "/";
        }
        return string.toString();
    }

    // TODO Document.
    private void source(Document document, File directory, String... path) {
        File file = Files.file(directory, path);
        if (file.isDirectory()) {
            Element reference = document.elements("/classpath/classpathentry[@kind = 'con']").get(0);
            Element source = document.createElement("classpathentry", null);
            source.setAttribute("kind", "src");
            source.setAttribute("path", join(path));
            if (path[2].equals("resources")) {
                source.setAttribute("excluding", "**/*.java");
            }
            if (path[1].equals("test")) {
                source.setAttribute("output", "target/test-classes");
            }
            reference.getParentNode().insertBefore(source, reference);
        }
    }

    // TODO Document.
    public void execute(Environment env) {
        Mix mix = env.get(Mix.class, 0);
        env.verbose("start", env.commands, mix.getWorkingDirectory());

        Serializer serializer = new Serializer();

        Project project = env.get(Project.class, 0);
        String name = project.getProductions().get(0).getArtifact().getName();
        
        File eclipse = new File(mix.getWorkingDirectory(), ".project");
        if (!eclipse.exists()) {
            Document document = serializer.load(getClass().getResourceAsStream("project.xml"), "project.xml");
            document.elements("/projectDescription/name").get(0).setTextContent(name);
            if (description == null) {
                description = "No description.";
                File readme = new File(mix.getWorkingDirectory(), "README");
                if (readme.exists()) {
                    List<String> lines = Files.slurp(readme);
                    if (lines.size() > 2) {
                        description = lines.get(2).trim();
                    }
                }
            }
            document.elements("/projectDescription/comment").get(0).setTextContent(description);
            serializer.write(document, eclipse);
        }

        File classpath = new File(mix.getWorkingDirectory(), ".classpath");
        if (!classpath.exists()) {
            Document document = serializer.load(getClass().getResourceAsStream("classpath.xml"), "classpath.xml");
            source(document, mix.getWorkingDirectory(), "src", "main", "java");
            source(document, mix.getWorkingDirectory(), "src", "main", "resources");
            source(document, mix.getWorkingDirectory(), "src", "test", "java");
            source(document, mix.getWorkingDirectory(), "src", "test", "resources");
            if (includeMix) {
                source(document, Files.file(mix.getWorkingDirectory(), "src", "mix", "java"));
                source(document, Files.file(mix.getWorkingDirectory(), "src", "mix", "resources"));
            }
            serializer.write(document, classpath);
        }

        Document document = serializer.load(classpath);

        for (Element element : document.elements("/classpath/classpathentry")) {
            if (element.getAttribute("kind").getTextContent().equals("var")) {
                element.remove();
            }
        }

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

        for (Artifact artifact : artifacts) {
            env.debug("add", artifact);
            ArtifactPart artifactPart = env.library.getArtifactPart(artifact);
            File directory = artifactPart.getLibraryDirectory();
            File jar = new File(directory, artifact.getPath("jar"));
            if (!jar.exists()) {
                throw new MixError(EclipseCommand.class, "jar.missing", artifact);
            }
            String variable = variables.get(directory);
            Element part = document.createElement("classpathentry", null);
            part.setAttribute("kind", "var");
            part.setAttribute("path", variable + "/" + artifact.getPath("jar"));
            File source = new File(directory, artifact.getPath("sources/jar"));
            if (source.exists()) {
                part.setAttribute("sourcepath", variable + "/" + artifact.getPath("sources/jar"));
            }
            File javadoc = new File(directory, artifact.getPath("javadoc/jar"));
            if (javadoc.exists()) {
                Element attributes = document.createElement("attributes", null);
                Element attribute = document.createElement("attribute", null);
                attribute.setAttribute("value", "jar:" + javadoc.toURI().toASCIIString() + "!/");
                attribute.setAttribute("name", "javadoc_location");
                attributes.appendChild(attribute);
                part.appendChild(attributes);
            }
            document.getFirstChild().appendChild(part);
        }
        
        serializer.write(document, classpath);
    }
}
