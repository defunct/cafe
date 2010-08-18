--- layout: default
title: Mix Concerns and Decisions
---
General concerns about Mix.

## Emitting POM

I've often considered emitting a `pom.xml` file or a `build.xml` file for those
people that want to use their existing build system. It is a fig leaf to tell
someone who is resistant, that if they install Cafe, then they can have Cafe
generate a build file for Maven or Ant. Once they get to the point where they
can install Maven or Ant, they are already at the point where they can build
using Cafe, so the trick is to get them to go through the means for their ends.

Using a declarative domain-specific language would allow you to follow one route
to execute the build, another route to generate a build file. However, I'm now
leaning toward defining projects and project outlines as nested classes,
probably ones that simply implement executable, so that the outlines are defined
using the same expressions as any extensions. The tasks become easy to invoke
single actions, but that means that there is no meta defintion of the build.

Not that there is one now, but it is more likely. Moving toward expression the
build in Java statements, instead of in domain-specific language statments,
moves away from having a build definition that you can translate into another
build tool.

Which comes down to this: do you move toward a more expression build language,
or do you move toward providing backwards compatability. The mindset that I once
employed would tell me that, the generalization would provide opportunties for
unanticipated implementations. In typical, breathless exposition on the project
website, I'd describe the meta project defintion, which can be used to generate
`pom.xml` or `build.xml` or build directly, plus once could imagine parallelized
build solutions, and solutions that use algorithms from outer space.

The notion that generalization unlocks doors is one I eschew. I really just
binds you to abstraction. One that is talored to the two headed use case that
originated the design, that is an inflexible as a commitment to an
implementation. That is, the world of one, two or many does not apply here. You
only have two, build or generate a build XML, not three, build Cafe, Maven or
Ant.

Maven went the route of POM. That didn't work out so well, did it? In reality,
there is little you can do besides a basic build, without wading into a morass
of SoC'd fits and starts (SoC means I don't know why anything happens, creating
layers of abstraction, complexity, adding levels of indirection for
indirection's sake, because indirection gives you the appearence of pluggablity,
or future-proofing).

You see these extra nipples all the time in Java. While building a file comfort
library, it occurs to someone that a ZIP file is kind of like a little file
system, so I'll make the file system pluggable so you can treat a ZIP file like
a file system, transparently, and you could also define your own file system,
like by using databases from outer space. You end up with a library bulked up by
the abstraction, constrained by the limits of the ZIP as file system library,
with a snowball's chance in hell that anyone is ever going to turn to your
library's interfaces for a gateway to their distributed file system. 

Finally, there is the principle of the matter. Leaving aside the obvious
comparisons betweem Microsoft and Apple, the burden placed on developers when
you drop backwards compatability are distributed to other develepers,
administrators and users, as Apple believes, and as Apple drops legacy APIs and
moves forward, but leave that aside.

If you add this complexity to Cafe, so that you can placate people who are
disgunrtled that you've build your own build tool, if your solutions requires
first that Cafe run correctly, so that it can hand off to Maven or Ant, then you
you are pursing an utterly pointless build strategy, for the sake of people who
are not using your build system on principle, at the expense of people who would
use your build system gladly, if you focused your attention solely on making the
build system easy to use, not on ingratating yourself to people who argue the
appeal to tradition.

If you leave this baggage on the ground, you will fly higher.

Arbitrage. Complexity arbitrage. Take it to the bank, Alan. See what it's worth.

Don't play the same numbers as everybody else.

Of course, an amusing solution would be to generate a `build.xml` that simply
invoked Cafe as a command line application. Passing the target from Ant to Cafe
would be too much for Ant. I'm sure it could not be done without a great many
extensions and configurations.

Cafe is an elegant expression of my hatred of Ant and Maven. (Apache has some
really great ideals about open source and intellectual property. Too bad about
their software.)

## Domain Specific Language

The domain specific langauge might be a too cute way of trying to get Make, Rake
or SCons like brevity, when Java is simply more prolix. If you're able to
construct your tasks in the IDE, with completion, then it might not be quite so
onerous to build your targets in Java. The convience methods of Comfort I/O
make more sense than that the declarative style imposed by the domain-specific
language. The domain-specific langauge still has a place, as it is useful to
express dependencies and manage class paths.

Some helper methods, static of course, can extract properties from the
Jav-a-Go-Go `Environment`. The default project oulines will definately favor
convention over configuration, so that there is no need to fuss with non-sense
like customer source or target directory layout. You're welcome to create your
own outline. Outlines are just not that reusable.

## Working from an IDE

Creating a project might be simpiler too, if the project could be brought into
Eclipse, where it could be run and tested. Could you run the build, maybe from a
main method generated in the project?

## Fast Starts

Currently, I start a project by copying a file. A generator would be nice. One
that would simply build the directory structure and emit a project defintion.
The utility would take a class name as a parameter. It would start out wtih a
unit test framework as a dependency.

## Development Versus Test

Found a need for development versus test. String Beans JPA requires Hibernate,
in order to draw in the `javax.persistence` classes. I'm able to use Hibernate,
because my applications use Hibernate, but really, I want to just compile
against `javax.persistence`. My thought was to have a compilation group that
would be excluded.

But, now I'm reconsidering. It's not my fault that no one has gone to the
trouble to package `javax.persistence` correctly, and I'd rather have someone
exclude the bad packaging of `javax.persistence` rather than support the
unlikely case of dependencies needed for compilation that are not needed for
redistribution. That will still exist, but only in the very special case of
staticly bound pluggable components, as in SLF4J.

But, then accomodating the Java ecosystem as it is will be easier on the
innocents, then designing for the Java ecosystem as it should be.

The current cookbook builds its own dependencies file, so it could simply choose
the right target for that dependencies file, and also create a compile and
develop dependencies file.

Need to avoid splintering this into the half-dozen different dependency groups
that come with Maven. 

HEY!

What if we just make it easy to use exclusions? There ought to be one and only
one universal implementation of an interface, but if we've got that all muddy,
as it is the case with JPA annotations at the moment, then we can go ahead and
exclude the implementation that we disfavor.

Or else it could be a matter of the import order.

## Statements and Clauses

A clause is a part of a statement, but that does not mean that a statement
cannot nest inside a clause. This isn't a perfect analogy with middle-school
sentance diagrams.

## Classpaths to Launch Custom Tasks

Below is a somewhat muddled discusison of what classpaths mean to recipes.
There's also the issue of extra Tasks, like say, Antlr, which is the first one
I'll need to create. How do we adjust the classpath so that the task is
available to to the project definition?

I've decided that there is a separate facility for manipulating the Task
classpath, files that will be loaded along with path for main and resources.

## Creating New Tasks and Classpaths

So far, every Task that needs to work with the classpath is either acting
simply, printing the classpath to file, or else it is actually firing off a new
java process. I've not needed to try to employ the `ClassLoader` tickery of
Jav-a-Go-Go.

Tasks may be limited in scope. They make little programs, but not quite. It
might be that Spawn is the univeral command interpreter, which you've
suspsected, but the Tasks forms a domain specific language that is IDE driven.

There is a lot that can be done in that language, copying files and what not, to
create little scripts, or wrap around common utilities.

But, it gets tedious to do other things, such as upload to GitHub. I'd hate to
have to write that as a series of Commandables.

Again, seems like a series of Commandables is useful, but the pursuit of some
form of purity, where everything is a task, means building conditionals into the
little language, which will create something verbose and primitive.

Commandable is fine when something has only one logical outcome, such as mix
github upload, why do you need to create a recipe for that? It knows to build
the distribution recipe and upload the results.

Then, too, I'm liking the idea of creating Spawn based Jav-a-Go-Go programs.

Finally, I like the idea of freezing some commands, parsing comand lines, but if
we need conditionals, better to use Spawn.

Finally, recall that Java will always be more verbose, so you might as well use
Java, instead of trying to create a new scripting langauge. (You know, a Groovy
Jav-a-Go-Go extension would be rather powerful, same with jRuby, and that might
be the glue language that people can use in their builds, but I prefer Java.)

So...

Tasks are soley for operations on that which either produces bytecode or depends
on bytecode. They are not for describing how to upload files or checkout code.

They are commands that are easy to type out, because of the IDE and the domain
specific builder language. They do make some things easy. I've thought about
removing the file operation tasks, to cut with a knife of conceptual purity, but
they do come in handy.

Tasks basically turn something that is full of fiddly switches into something
that is easy to peck out using an IDE and completion. They are part of that end
of the user experience.

Classpaths...

So far, any task that is not doing a little file operation will adjust the
classpath, reference what is in the classpath, by launching a new java process.

Antlr was the first extension task, and sure enough, the easiest way to get the
classpath correct was to spawn java within.

## Are Tasks and Commands the Same Thing?

One thing that keeps coming up is, what is a task? Why not just use a command?
Why is Mix Cobertura a command, but TestNG as task? Why can't Cobertura also be
a Task?

There is some entropy here. The notion of Ant tasks did not become so universal
that you'd want to program in a series of tasks instead of in a Turing machine
of some sort, say Java itself. Maven went another route and made everything
driving of the convention of the file system layout with a great heap big many
configuration variables.

Mix is not inbetween, it is off to the side.

Jav-a-Go-Go makes it easy to create new commands. Something like Cobertura can
operate nicely as a command, but the funny thing is that I need to create a
shell program to make it usable.

Same thing goes for where I want to go with cups, where we can say that by
default Hibernate 3.2 installs from Maven, while Hibernate 3.5 installs from
remix. At this level, again, I'd like to invoke arguments, so maybe...

Maybe, spawn is used as a cross-platform shell programming langauge?

Or do you create a very simple command interpreter, that can part a command
line? Probably, can. Probably as a single function that returns an array.

Add that to the toolbox. It would seem that you'd have a myriad of ways to
address different problems, rather than trying to find a unifying concept.

## Mutable Recipes Versus an Army of Participants

About to go the route of making recipes mutable from the builder, because when
broke out the JavaProject cookbook, it exposed its dependency on the Dependency
implementations. Here's an instance where favoring immutability is going to
create a one of those overtly builder like APIs, with yet another layer of
indirection, or else the exposure of a lot of implementation classes. Currently,
to build a set of dependencies without the builder, as you may wish to do if you
are creating a cookbook, you'll build a map of dependencies and then add those
dependencies to the recipe by passing the map. This means that the differents
sorts of dependency types must be exposed, so we're breaking open the package
implementation details, just to expose the constructor, just to have some air of
immutability, which really isn't there anyway.

Instead, the user can build a recipe for the sake of gathering dependencies,
then reference those dependencies by saying that a new recipe is dependent on
the dependency gathering recipe.

Thus, the ever more mutable builder language will allow people to start from a
cookbook, then adjust as they see fit. There will not be an army of constructor
participants.

## From Things

 * Error message for no project file. - It should read: Cannot find project
   file. You are currently in directory: /full/path
 * Replace InfuseExcpetion dependency on ContextualDanger.
 * Breadth first siblings, will take care of most problems?
 * Replace GoException with Danger.
 * Derive GoError from Danger.
 * Remove all of the nonsense double throws in Infuse.
 * Implement Jav-a-Go-Go stack trace errors. - How about --verbose --verbose
   prints the strack trace?
 * Where is Include.getVersionSelector ever used?
 * Time for convert the Mix DSL to use strings instead of files. - Tedious
   though, because it will mean changing a lot of code and breaking some builds.
 * Rename relativize to absolutize.
 * CodedDanger should be CodedException.
 * ContextualDanger should be ContextualException.
 * Implement compilation scope.
 * Rename Mix to Cafe
 ** Deploy Cafe at Solidica.
 ** Deploy Cafe at Xerox PDF.
 * Parse a Java source file for X-Ray Specs
 ** Check in source code for now.
 ** Turn the parser into a tree parser.
 ** Attempt to determine where I can find a line number.
 ** What to do about all the dead code in ANTLR generated source? - Can I make
    this part of the application separate somehow?
 ** Update Antlr task to use Antlr 3.
 * Comfort XML Prefix Update - Are up updating the prefixes correctly? When you
   descend the tree, do you update your context? What happens if a namespace is
   unmapped? No decisions made here yet. I'll add it to concerns and remove the
   document TODOs.
 * Optional Pretty Print of Comfort XML - Make pretty printing optional.
 * Make Test Does Not Work With Siblings - Try running it from paste.
 * Implement Project Namespace - Create a namespace for "project" variables and
   permit aguments to be attached directly to ProjectModule classes.
 ** Extract the argument reflection from CommandNode.
 ** Add the namespace specification to the Command annotation.
 ** Ignore the namespaced arguments during argument processing.
 * Create a ClassInfuser
 ** Create the ClassInfuser class.
 * Coverage for Jav-a-Go-Go
 ** Coverage for the library package.
 ** Coverage for Exit and see how much that fills in.
 ** Implement fork by calling fork in program queue and see what that covers.
 ** Test the program queue loop and determine how to trigger the tread count
    greater than zero branch, if possible.
 ** Test the negation of boolean values, write code that ensures that you can
    even start a property with no- and have it become no-no-.
 ** Test command line generator methods in environment.
 ** Test artfacts file reader and cleanup nonsense in the logic.
 * Javadoc for Spawn
 ** Write Javadoc for AbstractProgram.
 ** Write Javadoc for ByteSink.
 * Javadoc for Jav-a-Go-Go
 ** Write Javadoc for ArtifactsReaderTest.
 * Eclipse Linux
 ** Install Mix. - Probably can recursively copy from your repository if you
    mount it in VirtualBox.
 ** Build Verbiage using Mix. - It is the easiest thing to build there.
 * Eclipse Windows. - Build your software on Windows.
 ** Place git in the path.
 ** Create a zip file of your current repository and share it with your Windows
    installation.
