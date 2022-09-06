package concurrency.stage0;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 스레드 풀은 무엇이고 어떻게 동작할까? 테스트를 통과시키고 왜 해당 결과가 나왔는지 생각해보자.
 * <p>
 * Thread Pools https://docs.oracle.com/javase/tutorial/essential/concurrency/pools.html
 * <p>
 * Introduction to Thread Pools in Java https://www.baeldung.com/thread-pool-java-and-guava
 */
class ThreadPoolsTest {

    private static final Logger log = LoggerFactory.getLogger(ThreadPoolsTest.class);

    @Test
    void testNewFixedThreadPool() {
        /**
         * newFixedThreadPool
         * 공유 무제한 대기열에서 작동하는 고정된 수의 스레드를 재사용하는 스레드 풀을 만듭니다.
         * 어느 시점에서든 기껏해야 nThreads 스레드가 활성 처리 작업이 됩니다.
         * 모든 스레드가 활성 상태일 때 추가 작업이 제출되면 스레드를 사용할 수 있을 때까지 대기열에서 대기합니다.
         * 종료 전에 실행 중 실패로 인해 스레드가 종료되면 후속 작업을 실행하는 데 필요한 경우 새 스레드가 그 자리를 대신합니다.
         * 풀의 스레드는 명시적으로 종료될 때까지 존재합니다.
         */
        final var executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        executor.submit(logWithSleep("hello fixed thread pools"));
        executor.submit(logWithSleep("hello fixed thread pools"));
        executor.submit(logWithSleep("hello fixed thread pools"));

        // 올바른 값으로 바꿔서 테스트를 통과시키자.
        final int expectedPoolSize = 2;
        final int expectedQueueSize = 1;

        assertThat(expectedPoolSize).isEqualTo(executor.getPoolSize());
        assertThat(expectedQueueSize).isEqualTo(executor.getQueue().size());
    }

    @Test
    void testNewCachedThreadPool() {
        /**
         * newCachedThreadPool
         * 필요에 따라 새 스레드를 생성하지만 이전에 생성된 스레드가 사용 가능할 때 재사용하는 스레드 풀을 생성합니다.
         * 이러한 풀은 일반적으로 많은 단기 비동기 작업을 실행하는 프로그램의 성능을 향상시킵니다.
         * 실행 호출은 사용 가능한 경우 이전에 구성된 스레드를 재사용합니다.
         * 기존 스레드를 사용할 수 없으면 새 스레드가 생성되어 풀에 추가됩니다.
         * 60초 동안 사용되지 않은 스레드는 종료되고 캐시에서 제거됩니다.
         * 따라서 충분히 오랫동안 유휴 상태를 유지하는 풀은 리소스를 소비하지 않습니다.
         */
        final var executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        executor.submit(logWithSleep("hello cached thread pools"));
        executor.submit(logWithSleep("hello cached thread pools"));
        executor.submit(logWithSleep("hello cached thread pools"));

        // 올바른 값으로 바꿔서 테스트를 통과시키자.
        final int expectedPoolSize = 3;
        final int expectedQueueSize = 0;

        assertThat(expectedPoolSize).isEqualTo(executor.getPoolSize());
        assertThat(expectedQueueSize).isEqualTo(executor.getQueue().size());
    }

    private Runnable logWithSleep(final String message) {
        return () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info(message);
        };
    }
}
