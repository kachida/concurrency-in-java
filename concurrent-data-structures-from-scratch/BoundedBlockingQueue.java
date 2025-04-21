import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBlockingQueue<T> {

    private Lock lock = new ReentrantLock();
    private Condition notFullCondition = lock.newCondition();
    private Condition notEmptyCondition = lock.newCondition();
    private Queue<T> queue;
    private int capacity;

    public BoundedBlockingQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new LinkedList<>();
    }

    public void enqueue(T item) {
        lock.lock();
        try {
            while(queue.size() == capacity) {
                notFullCondition.await();
            }
            queue.add(item);
            notEmptyCondition.signal();

        } finally {
            lock.unlock();
        }
    }

    public E dequeue()  {
        lock.lock();
        try {
            while(queue.isEmpty()) {
                notEmptyCondition.await();
            }
            T item = queue.remove();
            notFullCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }

}


