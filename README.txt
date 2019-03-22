Project Development Requirements
- IntelliJ IDEA Ultimate 2018
- jdk 8u202
- Derby 10.14.2.0 stored in /jdk1.8.0_202/db/
- JFoenix .jar stored in /jdk1.8.0_202/jfoenix/


// -------------------------------------------------------
To assemble a jar file for your project, run the "jar" gradle task, either through IntelliJ or by doing
`gradle jar` on a terminal. Gradle will automatically download all dependencies needed to compile your jar file,
which will be stored in the build/libs folder.

Make sure to edit the main class attribute the build.gradle file, you'll need to change it in order to obtain
a working jar file.
