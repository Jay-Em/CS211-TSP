public class Semaphore {
	private boolean access;
	public Semaphore(){
		access = true;
	}
	public boolean check(){
		return access;
	}
	public synchronized void grab(){
			while (access == false) {
				try {
					wait();
				} catch (InterruptedException e) {
				}
			access = false;
		}
	}
	public synchronized void release(){
		access = true;
		notify();
	}
}
