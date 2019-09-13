package me.johnnyapol.Mira.Tasks;

import java.io.File;

import org.eclipse.jgit.api.Git;

import me.johnnyapol.Mira.System.WrappedProcess;
import me.johnnyapol.Mira.Utils.Properties;

public class GitDeployTask extends Task {
	
	public GitDeployTask(WrappedProcess process) {
		super(process);
	}

	@Override
	public String getName() {
		return "GitDeploy";
	}

	@Override
	public void execute()throws Throwable {
		Properties config = this.getProcess().getProcessConfiguration();
		
		Git git = Git.open(new File(config.getString("git-repo")));
		
		git.checkout()
			.setCreateBranch(false)
			.setName("git-branch")
			.call();
		
	}

	@Override
	public boolean isRepeatedTask() {
		return true;
	}

	@Override
	public long getRepeatInterval() {
		return 24 * 60 * 60 * 1000;
	}

}
