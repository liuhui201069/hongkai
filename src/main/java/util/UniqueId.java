package util;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.apache.commons.text.RandomStringGenerator;

/**
 * @author huiliu
 * @date 17/10/22
 */
public class UniqueId {
    /**
     * 生成随机字符串
     */
    private static UniformRandomProvider RNG = RandomSource.create(RandomSource.MT);
    private static RandomStringGenerator GENERATOR = new RandomStringGenerator.Builder()
        .withinRange('a', 'z')
        .usingRandom(RNG::nextInt)
        .build();
    private static Integer TRACE_ID_LENGTH = 20;

    /**
     * 生成唯一ID
     * @return
     */
    public static String getUniqeId(){
        return GENERATOR.generate(TRACE_ID_LENGTH);
    }
}
