import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class JavaThreadSynchronization {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        boolean[] isOddTurn = {true};

        Runnable printOdd = () ->  {
            for (int i=1; i<=10 ;i+=2) {
                lock.lock();
                try {
                    while(isOddTurn[0] == false){
                        condition.await();
                    }
                    System.out.println(Thread.currentThread().getName() + ": " + i );
                    isOddTurn[0] = false;
                    condition.signal();
                } catch (Exception e) {

                } finally {
                    lock.unlock();
                }
            }
        };

        Runnable printEven = () -> {
            for (int i = 2; i<=10; i+=2) {
                lock.lock();
                try {
                    while(isOddTurn[0] == true) {
                        condition.await();
                    }
                    System.out.println(Thread.currentThread().getName() + ": " + i );
                    isOddTurn[0] = true;
                    condition.signal();
                } catch (Exception e) {

                } finally {
                    lock.unlock();
                }
            }
        };

        Thread t1 = new Thread(printOdd, "OddThread");
        Thread t2 = new Thread(printEven, "EvenThread");

        t1.start();
        t2.start();

        t1.join();
        t2.join();



    }
}
