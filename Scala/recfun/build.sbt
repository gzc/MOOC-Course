name <<= submitProjectName(pname => "progfun-"+ pname)

version := "1.0.0"

scalaVersion := "2.11.5"

scalacOptions ++= Seq("-deprecation", "-feature")

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"

libraryDependencies += "junit" % "junit" % "4.10" % "test"

// This setting defines the project to which a solution is submitted. When creating a
// handout, the 'createHandout' task will make sure that its value is correct.
submitProjectName := "recfun"

libraryDependencies ++= {
  val c = currentProject.value
  if (c.isEmpty || c == "quickcheck") Seq(
    "org.scalacheck" %% "scalacheck" % "1.12.1"
  )
  else Seq.empty
}

libraryDependencies ++= {
  val c = currentProject.value
  if (c.isEmpty || c == "nodescala" || c == "suggestions") Seq(
    "com.netflix.rxjava" % "rxjava-scala" % "0.15.0",
    "org.json4s" %% "json4s-native" % "3.2.11",
    "org.scala-lang.modules" %% "scala-swing" % "1.0.1",
    "net.databinder.dispatch" %% "dispatch-core" % "0.11.0",
    "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    "org.slf4j" % "slf4j-api" % "1.7.5",
    "org.slf4j" % "slf4j-simple" % "1.7.5",
    "com.squareup.retrofit" % "retrofit" % "1.0.0",
    "org.scala-lang.modules" %% "scala-async" % "0.9.2"
  )
  else Seq.empty
}

libraryDependencies ++= {
  val c = currentProject.value
  if (c.isEmpty || c == "actorbintree" || c == "kvstore") Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.3.9",
    "com.typesafe.akka" %% "akka-testkit" % "2.3.9"
  )
  else Seq.empty
}

libraryDependencies ++= {
  val c = currentProject.value
  if (c.isEmpty || parProgProjects(c)) Seq(
    "com.storm-enroute" %% "scalameter-core" % "0.6",
    "com.github.scala-blitz" %% "scala-blitz" % "1.1",
    "com.storm-enroute" %% "scalameter" % "0.6" % "test"
  )
  else Seq.empty
}

fork := {
  val c = currentProject.value
  if (parProgProjects(c)) true
  else false
}

// See documentation in ProgFunBuild.scala
projectDetailsMap := {
val currentCourseId = "progfun-005"
Map(
  "example" ->  ProjectDetails(
                  packageName = "example",
                  assignmentPartId = "gTzFogNk",
                  maxScore = 10d,
                  styleScoreRatio = 0.2,
                  courseId=currentCourseId),
  "recfun" ->     ProjectDetails(
                  packageName = "recfun",
                  assignmentPartId = "4Rarn9Ki",
                  maxScore = 10d,
                  styleScoreRatio = 0.2,
                  courseId=currentCourseId),
  "funsets" ->    ProjectDetails(
                  packageName = "funsets",
                  assignmentPartId = "gBXOL7Rd",
                  maxScore = 10d,
                  styleScoreRatio = 0.2,
                  courseId=currentCourseId),
  "objsets" ->    ProjectDetails(
                  packageName = "objsets",
                  assignmentPartId = "25dMMEz7",
                  maxScore = 10d,
                  styleScoreRatio = 0.2,
                  courseId=currentCourseId),
  "patmat" ->     ProjectDetails(
                  packageName = "patmat",
                  assignmentPartId = "6gPmpcif",
                  maxScore = 20d,
                  styleScoreRatio = 0.2,
                  courseId=currentCourseId),
  "forcomp" ->    ProjectDetails(
                  packageName = "forcomp",
                  assignmentPartId = "gG3oZGIO",
                  maxScore = 10d,
                  styleScoreRatio = 0.2,
                  courseId=currentCourseId),
  "streams" ->    ProjectDetails(
                  packageName = "streams",
                  assignmentPartId = "1WKgCFCi",
                  maxScore = 20d,
                  styleScoreRatio = 0.2,
                  courseId=currentCourseId),
  "quickcheck" -> ProjectDetails(
                  packageName = "quickcheck",
                  assignmentPartId = "02Vi5q7m",
                  maxScore = 10d,
                  styleScoreRatio = 0.0,
                  courseId=currentCourseId),
  "simulations" -> ProjectDetails(
                  packageName = "simulations",
                  assignmentPartId = "pA3TAeu1",
                  maxScore = 10d,
                  styleScoreRatio = 0.0,
                  courseId=currentCourseId),
  "nodescala" -> ProjectDetails(
                  packageName = "nodescala",
                  assignmentPartId = "RvoTAbRy",
                  maxScore = 10d,
                  styleScoreRatio = 0.0,
                  courseId=currentCourseId),
  "suggestions" -> ProjectDetails(
                  packageName = "suggestions",
                  assignmentPartId = "rLLdQLGN",
                  maxScore = 10d,
                  styleScoreRatio = 0.0,
                  courseId=currentCourseId),
  "actorbintree" -> ProjectDetails(
                  packageName = "actorbintree",
                  assignmentPartId = "VxIlIKoW",
                  maxScore = 10d,
                  styleScoreRatio = 0.0,
                  courseId=currentCourseId),
  "kvstore"      -> ProjectDetails(
                  packageName = "kvstore",
                  assignmentPartId = "nuvh59Zi",
                  maxScore = 20d,
                  styleScoreRatio = 0.0,
                  courseId=currentCourseId),
  "constraints"  -> ProjectDetails(
                  packageName = "constraints",
                  assignmentPartId = "kL1K2FAj",
                  maxScore = 10d,
                  styleScoreRatio = 0.0,
                  courseId=currentCourseId),
  "interpreter"  -> ProjectDetails(
                  packageName = "interpreter",
                  assignmentPartId = "1SZhe1Ua283r87a7rd",
                  maxScore = 10d,
                  styleScoreRatio = 0.0,
                  courseId=currentCourseId),
  "scalashop"    -> ProjectDetails(
                  packageName = "scalashop",
                  assignmentPartId = "TODO <-- we need to generate this one",
                  maxScore = 10d,
                  styleScoreRatio = 0.0,
                  courseId=currentCourseId),
  "reductions"   -> ProjectDetails(
                  packageName = "reductions",
                  assignmentPartId = "TODO <-- we need to generate this one",
                  maxScore = 10d,
                  styleScoreRatio = 0.0,
                  courseId=currentCourseId)
)}

// Files that we hand out to the students
handoutFiles <<= (baseDirectory, projectDetailsMap, commonSourcePackages, parProgCommonSourcePackages) map {
  (basedir, detailsMap, commonSrcs, parProgCommonSrcs) =>
  (projectName: String) => {
    val details = detailsMap.getOrElse(projectName, sys.error("Unknown project name: "+ projectName))
    val commonFiles = (PathFinder.empty /: commonSrcs)((files, pkg) =>
      files +++ (basedir / "src" / "main" / "scala" / pkg ** "*.scala")
    )
    val parProgCommonFiles =
      if (parProgProjects(projectName)) (PathFinder.empty /: parProgCommonSrcs)((files, pkg) =>
        files +++ (basedir / "src" / "main" / "scala" / pkg ** "*.scala")
      )
      else PathFinder.empty

    (basedir / "src" / "main" / "scala" / details.packageName ** "*.scala") +++
    commonFiles +++
    parProgCommonFiles +++
    (basedir / "src" / "main" / "resources" / details.packageName ** "*") +++
    (basedir / "src" / "test" / "scala" / details.packageName ** "*.scala") +++
    (basedir / "build.sbt") +++
    (basedir / "project" / "build.properties") +++
    (basedir / "project" ** ("*.scala" || "*.sbt")) +++
    (basedir / "project" / "scalastyle_config.xml") +++
    (basedir / "project" / "scalastyle_config_reactive.xml") +++
    (basedir * (".classpath" || ".project")) +++
    (basedir / ".settings" / "org.scala-ide.sdt.core.prefs")
  }
}

// This setting allows to restrict the source files that are compiled and tested
// to one specific project. It should be either the empty string, in which case all
// projects are included, or one of the project names from the projectDetailsMap.
currentProject := ""

// Packages in src/main/scala that are used in every project. Included in every
// handout, submission.
commonSourcePackages += "common"

parProgCommonSourcePackages := Seq(
  "parprogcommon"
)

// Packages in src/test/scala that are used for grading projects. Always included
// compiling tests, grading a project.
gradingTestPackages += "grading"
