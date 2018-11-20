package com.shsxt.xmjf.api.constants;

import java.io.FileWriter;
import java.io.IOException;

/**
 * @author zhangxuan
 * @date 2018/11/19
 * @time 19:06
 */
public class AlipayConfig {
    //↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016092200569525";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCjnBDb3ph1fTmRZRXM3atTphJB+Wn/bjL5uiHMGTHMdN7v9iig2EcscpMzrEkcmO54T7gJz6K0fFdEqmnpIP4/l3+Bfr0qUKhaj/7A0XHmevDh2e27X7Pe9VmS8K0xrXRTWAi1WpbaBPuEd6THvN5pW4mVHYvKhZV9dsdn7q1NehSFWuOZwuEROH+1NVf28SUyGZ1RJhSeYvuPeNjVFxpZ695eJbjh4mF9tGXFcdPZDCd2WeBdH7BSTaQyF8HLNboXv/yMfWoVbGrSSOsNIHh3N7MMEZhBVIIbKPfEWGy3ZGruJI+y76z2fKe+JQ7zZt4A78ZQGWpsG6EW+dQKjE6xAgMBAAECggEAf+paB/1eB6WzZ6i3xaed9tWFus0tAYcAFd1f5TnhhG/viX9UvoS6CNc/nYva0aAD+k6hnhIXrLL2a/osuheQkNl62Bzl+vp61et1Cv315nRCduIzC2dEs1HyCX9gedGafDr8/AVqoeR5XVVJoNxaq9bkl5A1Jk8Q1LoUJ38lQcerykNpcOpzgWSYWc3X9eNUIS1f1xcxdLZ1PHZDy3QOdwYwngfrqcC3t6mbYkgavo4DzkqRtciYVOIkl9L18A9XLJqO+Q6KW/ZodcXHSJkjN5eHpybbV7EVD79l1e0oe+O6qccIm8cnkjstGfdZBQJdZUbNmeAw50IeMpQsgZ39wQKBgQDP+s78MOS3HJtgjJTd6duyj427VcqouGxO1I2flqsXaTdT609tmVMmPFBiNvbzdK1hG0EqyomGyIwvqUTNraKy7g5rAKgNRVgS4eEhFeXAhQwtV95MJiIVRlRGS3TEJJJy4yyGPKzR2nVtmnrp83u2lVL5iI2vsi9ZhAI+SwIV+QKBgQDJYqVYljgEsdbQZY3ychxB7K1sDzJj1wPwm91wFV5yfWjnvIX08Nq6AxEIKJrqwDn4l08MQTtlVIYbqEb9ywVUag+ZWinpO7vPD/w0ofJ9TpdDlLOqwurGo4k84SFCb6LMDcc8uU5nxTnBx8Ccy/D1IWXluNphbpmpBXkZ0tpMeQKBgQCk5y1UwoOmOHVuESk75t++bZooLjbYnhmqS1fbcIBJuLAYxtucf9Rw3aOyq/gUQq/x7cU24441oy+b3q9iEUmOa7hc2RFmLFRP7G8vZ4hapGNqVGA6qoWdrrzFW7DCkGjZ1QwJh3RTk85F0BWjB95Qf4dhuQb2huaKKGQK9jg6KQKBgQCOVZpnskSFjOw2/pAE+4HdVlp8NYF371BpjsBDp9bJNLogj65eIJ13OxOeoJRdtt9CRC8qijBgvgzLH3SNjCCIOS4tRPy6S+3Bu5qV2KB60EE5ErCp0Hn/SzaEQNoxCpYVgud+k7pGva7Et1n7eGkl2CUc6DnxAyq3u5aMGesQgQKBgBldXhxDnoRR20FlbE9N6QEe6cEv1yYuDoid0fi3trlACU4TPV/PuaGhPe8yKO2pnS5WNMtSmUDbath0rS+xqFLeP6UxRp9T4ylugBrVa3nKOEETOQ5ACweIR5lOIlBIHPhbVErUQW6bCGK0/2aOYpwFg2gNDjprtDpil+W7nMER";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3szKwPVJu0hqq/aVBmyo1Tnsycq94dWibFG4U9I55R4qaAq3C0SMQLoxFiyoLN6HlJsAVMXLiKMOJoi+vevUDQYwAFEA5wwnSMrJS3rLb0uZ/STEN/cRfLRFLtqIP0AZxK8Pj1at82q7xPe7e1kRKB0hkfu3XQgyYZ1yXNkgHQg3c6B/2/4zJGme5zd6oqPP81Ty0ZNhjPp4xkUT+kfnKlDEGnbSjE7VdhV+n52BHGWPH1wPXIAmWBK/990fYAvZF+0xV/je2wbT4Huw6+8tyMTgFjw2rKrzWzsd5cpOIQfHkybvopOf2oEVe4W222WDVGovupWEKcgTX2vLlFP9rwIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "   http://ii5ksc.natappfree.cc/account/notifyUrl";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "   http://ii5ksc.natappfree.cc/account/returnUrl";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";

    //支付宝沙箱商家Id
    public static final String seller_id="2088102176753347";

    public static final String trade_status="TRADE_SUCCESS";
    public static final String notify_result="success";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
