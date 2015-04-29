/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */

package com.qingyuan.alipay;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

	//合作身份者id，以2088开头的16位纯数字
	public static final String DEFAULT_PARTNER = "2088901497661054";

	//收款支付宝账号
	public static final String DEFAULT_SELLER = "zhifubao@07919.com";

	//商户私钥，自助生成
	public static final String PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJKvjDoN7i6FaCJ7DUjYtVj61omJl9Bgl+4CYxvcwNBmeMimQwevVSNWncNkg4fLIncqIRRpanvG8WG9GnRqe1mNAzUcCi6xC2ZmjDaQ9T459z9b5eGOxO4SSD1vADI/W2CCpZipzZpnYVGvIlVTnfRN6OPvl5JK46pdXaFq3RUbAgMBAAECgYAv9LiRmlBOYUskSrqTIFZZmthc6uD5PTsukxU33Lok5/uBDepILuhUv1KguNHPqXfBIcLi23v2r5nm7F4jxVRB+gSaeoukygj5Wz08IZk6JBagbZTJuLu0keAc2ouXYkiey/vbNxd4mymQSY2EiakcP1Y2lxe0PPw14HfSAGOJEQJBAMJuUXzNfORXVzzGzXiAtFfdNbxo6cypYvbk2/RMesJVIou4c2xriKZfKalw8CigJlUvJXdY+54mhE86X1REm00CQQDBIrsm6xKz2qireP98/oS0trHlxrhFkUOvrgw1tVewo99UEOE+ZwT8XVOw73n3qqWbBebvLB/zQE36SJow6S4HAkA+WI+qvF26/sLLi1ghtuudbSKpCt9FsF86mkJpwQhwPkp4kSn57zV/W+6JFdz8ufXwxNB3x8HPgbxVh3tAE0vVAkEAwL8logCGaMI3BPI7oAXucN/HiPaanqsLrv93Jf615g6VajNyZ7GukyGczX8h7R78KUtpnpYdyMkLSVY9s8Ry7wJBAJ85G2ldhcCdkiRKOS2nk5oM+3Zqr3tgQCYQQsfwun6AgapIHgcppqS16+fEKztyyVgnB6sXsf7ymAQzh0sueFk=";

	//支付宝公钥
	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

}
