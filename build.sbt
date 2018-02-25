import sbt._
import sbt.Keys._
import com.typesafe.sbt.packager.docker.{Cmd, ExecCmd}
import NativePackagerHelper._

lazy val commonSettings = Seq(
  organization := "com.github.htch",
  scalaVersion := "2.11.11",
  // https://tpolecat.github.io/2014/04/11/scalac-flags.html
  scalacOptions ++= Seq(
    ""
    ,"-Xfatal-warnings"
    ,"-feature"
    ,"-deprecation"
    ,"-Ywarn-unused"
    ,"-Ywarn-unused-import"
    ,"-Xlint"
    ,"-unchecked"
    ,"-Yno-adapted-args"
    // ,"-Ywarn-dead-code"
    ,"-Ywarn-value-discard"
    ,"-Ywarn-infer-any"
    ,"-Ywarn-numeric-widen"
    // ,"-Ylog-classpath"
  )
)

lazy val example = project.in(file("."))
  .settings(Project.defaultSettings ++ commonSettings ++ Seq(
    name := "example",
    version := "0.0.1-SNAPSHOT",
    libraryDependencies ++= Seq(
    ),
    dockerBaseImage := "openjdk:8",
    dockerCommands := dockerCommands.value.filter {
      case ExecCmd("CMD", _*) ⇒ false
      case ExecCmd("ENTRYPOINT", _*) ⇒ false
      case _ ⇒ true
    },
    dockerCommands ++= Seq(
      Cmd("CMD", "bin/example")
    ),
    dockerAlias := DockerAlias(None, None, "htch/scala-docker-build-example", Some("latest"))
  )).enablePlugins(JavaServerAppPackaging).enablePlugins(DockerPlugin)