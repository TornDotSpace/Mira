package me.johnnyapol.Mira.Tasks;

import me.johnnyapol.Mira.System.WrappedProcess;

public class ProcessRestartTask implements Task {

	@Override
	public String getName() {
		return "ProcessRestartTask";
	}

	@Override
	public void execute(WrappedProcess process) throws Throwable {
		process.getProcessManager().restartProcess(process.getId());
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
