---
title: Mix Concerns and Decisions
---
General concerns about Mix.

## Fast Starts

How do you get a project running fast? It seems like it is pretty easy now,
actually, I simply copy over another project file, then start editing.

Is there a better way to get things going? Maybe, how do I jump start a project,
probably by typing the class name, which produces a class file, that can in turn
produce an Eclipse file, that has the Mix classes ready for editing in Eclipse.

Maybe.

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
