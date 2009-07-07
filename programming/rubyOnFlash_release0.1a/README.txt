Installation Guide for Ruby On Flash (ver. 0.1a)
http://sourceforge.net/projects/rubyonflash

Refer to gpl_License.txt for the GPL license

Software packages needed:
Java SDK: http://java.sun.com/javase/downloads/index.jsp
Apache Ant: http://ant.apache.org/
Java Cup: http://www2.cs.tum.edu/projects/cup/
JSwiff: http://www.jswiff.com/

Once you’ve got the above software packages installed, download Ruby On Flash’s source and copy it to a directory.  Note that this directory will also be Ruby On Flash’s installation directory.  

JSwiff modifications:
Copy the files, ActionBlock.java, If.java and Jump.java in the jswiff_mod directory, into your JSwiff directory’s src/com/jswiff/swfrecords/actions directory.  
Recompile JSwiff.  

Configure build.xml:
In build.xml, you should see the following commented out tags near the top of the file:
#######################################################################################
<!--
Input the full path name for the JSwiff library here!
<property name="jswiff.path" value="D:/HYP/jswiff/jswiff-8.0-beta-2.jar" />
-->
<!--Input the full path name for the Java Cup library here!
<property name="jcup.path" value="D:/CUP/java-cup-11a.jar" />
-->
#######################################################################################
Uncomment the above and edit the fields accordingly.  

Run ant in the installation directory.  
After execution, you’ll see 3 additional files in the installation directory:
1)	jcup.jar – A copy of your system’s java cup jar file, as indicated in the jcup.path property in build.xml.  .  
2)	jswiff.jar – A copy of your system’s jswiff jar file, as indicated in the jswiff.path property in build.xml.  
3)	RubyToSwf.jar – an executable jar for Ruby On Flash.  
In addition, a new folder, “Ruby” will be created.  

Installation is completed!!!
Refer to User’s Guide to start developing using Ruby On Flash now!
