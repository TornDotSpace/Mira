package me.johnnyapol.Mira.Tasks;

import me.johnnyapol.Mira.System.WrappedProcess;

public class GitDeployTask implements Task {
	
	@Override
	public String getName() {
		return "GitDeploy";
	}

	@Override
	public void execute(WrappedProcess process) throws Throwable {
		// TODO Auto-generated method stub
		
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
