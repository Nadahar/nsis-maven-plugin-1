/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.eti.jadler.nsis.maven.plugin;

import java.io.File;
import java.net.URL;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 *
 * @author Jaguaraquem A. Reinaldo <jaguar.adler@gmail.com.br>
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class Generate extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter(defaultValue = "${project.basedir}", readonly = true)
    private File basedir;

    @Parameter(defaultValue = "${project.build.directory}", readonly = true)
    private File target;

    @Parameter(defaultValue = "$PROGRAMFILES", required = true)
    private String installRoot;
    
    @Parameter(property = "nsis.muiIcon")
    private String icon;
    
    @Parameter(property = "nsis.muiUnIcon")
    private String unIcon;
    
    @Parameter
    private String extraPreInstallCommand;
    
    @Parameter
    private String extraInstallCommand;
    
    @Parameter
    private String extraUninstallCommand;
    
    @Parameter
    private Boolean enableUninstallBeforeInstall;
    
    @Parameter
    private Boolean modifyPath;
    
    @Parameter
    private String displayName;
    
    @Parameter
    private String packageName;
    
    @Parameter(defaultValue = "${project.build.finalName}")
    private String packageFileName;
    
    @Parameter
    private URL helpLink;
    
    @Parameter
    private URL urlInfoAbout;
    
    @Parameter
    private String contact;
    
    @Parameter
    private String installDirectory;
    
    @Parameter
    private String vendor;
    
    @Parameter
    private File outputFileName;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Hello, World");
    }

}
