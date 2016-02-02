/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.eti.jadler.nsis.maven.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 *
 * @author Jaguaraquem A. Reinaldo <jaguaraquem.adler@biologica.com.br>
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class Generate extends AbstractMojo {

    @Parameter(property = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Hello, World");
    }

}
