package $packageName.environment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 运行环境检测器，用判断代码是否运行在生产环境下。
 *
 * @author chenwei & hiyuno
 */
@Component
public class EnvironmentDetector {

    @Value("${$projectNamePropertyPrefix.environment}")
    private String environment = "";

    /**
     * 判断当前运行环境是否为生产环境。
     *
     * @return 是否为生产环境
     */
    public boolean isProduction() {
        return "rc".equals(environment);
    }
}