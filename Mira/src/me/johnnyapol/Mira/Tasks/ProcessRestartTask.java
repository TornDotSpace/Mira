package me.johnnyapol.Mira.Tasks;

import me.johnnyapol.Mira.System.WrappedProcess;

public class ProcessRestartTask extends Task {

	public ProcessRestartTask(WrappedProcess process) {
		super(process);
	}

	@Override
	public String getName() {
		return "ProcessRestartTask";
	}

	
	@Override
	public boolean isRepeatedTask() {
		return true;
	}

	@Override
	public long getRepeatInterval() {
		return 24 * 60 * 60 * 1000;
	}

	@Override
	public void execute() throws Throwable {
		this.getProcess().getProcessManager().restartProcess(this.getProcess().getId());
	}

}
