package cc.yuerblog;

import azkaban.alert.Alerter;
import azkaban.executor.ExecutableFlow;
import azkaban.sla.SlaOption;
import azkaban.utils.Props;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;

import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;

public class AzkabanDingDingAlert implements Alerter {
    private static final Logger logger = Logger.getLogger(AzkabanDingDingAlert.class);

    private String serverUrl;

    public AzkabanDingDingAlert(Props props) {
        serverUrl = props.get("dingding.serverUrl");
        logger.info("MyAlert construct..." + serverUrl);
    }

    private void sendDing(String msg)  {
        logger.info("sendDing, msg=" + msg);
        try {
            DingTalkClient client = new DefaultDingTalkClient(serverUrl);

            // 请求
            OapiRobotSendRequest request = new OapiRobotSendRequest();
            request.setMsgtype("text");

            // 文本附加到请求
            OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
            text.setContent(msg);
            request.setText(text);

            // 发送
            OapiRobotSendResponse response = client.execute(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private String dateFormat(long ts) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return fmt.format(ts);
    }

    @Override
    public void alertOnSuccess(ExecutableFlow executableFlow) throws Exception {
        logger.info("alertOnSuccess");
    }

    @Override
    public void alertOnError(ExecutableFlow executableFlow, String... strings) {
        logger.info("alertOnError");
        sendDing(String.format("[Error] azkaban projectId=%d flow=%s executionId=%d startTime=%s endTime=%s error=%s",
                executableFlow.getProjectId(),
                executableFlow.getFlowId(),
                executableFlow.getExecutionId(),
                dateFormat(executableFlow.getStartTime()),
                dateFormat(executableFlow.getEndTime()),
                String.join("\n", strings)
        ));
    }

    @Override
    public void alertOnFirstError(ExecutableFlow executableFlow) throws Exception {
        logger.info("alertOnFirstError");
    }

    @Override
    public void alertOnSla(SlaOption slaOption, String s) throws Exception {
        logger.info("alertOnSla");
    }
}
