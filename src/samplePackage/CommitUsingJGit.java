package samplePackage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class CommitUsingJGit {

	public static void main(String[] args) throws IOException, GitAPIException {

		Repository existingRepo = new FileRepositoryBuilder().setGitDir(new File("C:/eclipse/Workspace/SampleDemo")).build();

		// Monitor to get git command progress printed on java System.out
		// console
		TextProgressMonitor consoleProgressMonitor = new TextProgressMonitor(new PrintWriter(System.out));

		Git git = new Git(existingRepo);
		System.out.println("\n>>> Listing all branches\n");
		git.branchList().setListMode(ListMode.ALL).call().stream().forEach(r -> System.out.println(r.getName()));

		System.out.print("\n>>> Printing status of local repository\n");
		Status status = git.status().setProgressMonitor(consoleProgressMonitor).call();

		System.out.print("\n>>> Modified file\n");
		status.getModified().stream().filter(str -> str.endsWith(".java")).forEach(System.out::println);

		System.out.print("\n>>> Untracked/Newly Created file\n");
		status.getUntracked().stream().filter(str -> str.endsWith(".java")).forEach(System.out::println);

		System.out.print("\n>>> Added file\n");
		status.getAdded().stream().filter(str -> str.endsWith(".java")).forEach(System.out::println);

		// Commit a particular file in the project
		System.out.println("\n>>> Committing changes\n");
		RevCommit revCommit = git.commit().setOnly("src/test/java/HelloWorld.java").setMessage("Adding commit from JGIT")
				.call();

		System.out.println("Commit = " + revCommit.getFullMessage());

		// Providing credentials to push code to remote repository
		/*CredentialsProvider cp = new UsernamePasswordCredentialsProvider("Deepak12390", "Rehan@31");
		Iterable<PushResult> pushLog = git.push().setCredentialsProvider(cp).setPushAll().call();
		pushLog.forEach(r -> System.out.println(r.getMessages()));*/

		// Close Git
		git.close();

	}

}
