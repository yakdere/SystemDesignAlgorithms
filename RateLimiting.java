import java.util.concurrent.locks.ReentrantLock;
import java.util.LinkedList;
public class RateLimiting {
        private final int capacity;
        private final int outflowRate;
        private LinkedList<Long> queue;
        private final int refillRate;
        private int tokens;
        private long lastRefillTimestamp;
        private final ReentrantLock lock = new ReentrantLock();
    
    public RateLimiting(int capacity, int refillRatePerSecond) {
        this.capacity= capacity;
        this.refillRate = refillRatePerSecond;
        this.outflowRate = refillRatePerSecond;
    }
    
    public void useTokenBucketAlgoritm() {
        this.tokens = capacity;
        this.lastRefillTimestamp = System.nanoTime();
    }
        
    public void useLeakyBucketAlgorithm() {
        this.queue = new LinkedList<>();
    }
    
     public synchronized boolean allowRequestBucket() {
        long currentTime = System.currentTimeMillis();
        // if enough time passed poll until 
        while (!queue.isEmpty() && currentTime - queue.peek() >= (1000 / outflowRate)) {
            queue.poll();
        }
        if (queue.size() < capacity) {
            queue.offer(currentTime);
            return true;
        }
        return false;
     }


    public boolean allowRequestToken() {
        lock.lock();
        try {
            refillTokens();
            if (tokens > 0) {
                tokens--;
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }
        
    private void refillTokens() {
        long currentTime = System.nanoTime();
        long timeElapsed = currentTime - lastRefillTimestamp;
        int tokensToAdd = (int) ((timeElapsed / 1e9) * refillRate); // nanotime
        if (tokensToAdd > 0) {
            tokens = Math.min(capacity, tokens + tokensToAdd);
            lastRefillTimestamp = currentTime;
        }
    }
    
    public static void main(String args[]) {
    
    RateLimiting rateLimiting = new RateLimiting(10, 5);
    // rateLimiting.useTokenBucketAlgoritm();
    rateLimiting.useLeakyBucketAlgorithm();
    
    for (int i = 0; i<15; i++) {
        if(rateLimiting.allowRequestBucket()) {
            System.out.println("request " + i + " allowed.");
        } else {
            System.out.println("request " + i + " denied.");
        }
        
        try {
            Thread.sleep(500); // Sleep for demonstration purposes
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
  }
}