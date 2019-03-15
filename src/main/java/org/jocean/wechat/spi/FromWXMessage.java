package org.jocean.wechat.spi;


import javax.ws.rs.Consumes;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * 接收来自微信的消息 当普通微信用户向公众账号发消息时，微信服务器将POST消息的XML数据包到开发者填写的URL上。
 *
 *请注意：
 *
 *1、关于重试的消息排重，推荐使用msgid排重。
 *2、微信服务器在五秒内收不到响应会断掉连接，并且重新发起请求，总共重试三次。
 *   假如服务器无法保证在五秒内处理并回复，可以直接回复空串，微信服务器不会对此作任何处理，并且不会发起重试。详情请见“发送消息-被动回复消息”。
 *3、为了保证更高的安全保障，开发者可以在公众平台官网的开发者中心处设置消息加密。
 *   开启加密后，用户发来的消息会被加密，公众号被动回复用户的消息也需要加密（但开发者通过客服接口等API调用形式向用户发送消息，则不受影响）。关于消息加解密的详细说明，请见“消息加解密说明”
 *
 * MsgType字符串列表
 * 文本消息 text
 * 图片消息image
 * 语音消息voice
 * 视频消息 video
 * 小视频消息 shortvideo
 * 地理位置消息 location
 * 链接消息 link
 * 事件推送消息 event
 *    1 关注/取消关注事件
 *    2 扫描带参数二维码事件
 *    3 上报地理位置事件
 *    4 自定义菜单事件
 *    5 点击菜单拉取消息时的事件推送
 *    6 点击菜单跳转链接时的事件推送
 *
 */
@Consumes({"application/xml","text/xml"})
@JacksonXmlRootElement(localName="xml")
public class FromWXMessage {

    private String _msgId; //消息id，64位整型
    private String _toUserName; //开发者微信号
    private String _fromUserName; //发送方帐号（一个OpenID）
    private long   _createTime; //消息发送时间  the number of seconds since January 1, 1970, 00:00:00 GMT
    private String _msgType; //文本消息 text 图片消息image 语音消息voice 视频消息 video 小视频消息 shortvideo 地理位置消息 location  链接消息 link 等等
    private String _content; //文本消息内容
    private String Format; //Format为语音格式，一般为amr
    private String Recognition;//为语音识别结果，使用UTF8编码
    private String PicUrl; //图片链接
    private String MediaId;//媒体id，可以调用多媒体文件下载接口拉取数据
    private String ThumbMediaId;//视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据
    private String Location_X;// 	地理位置维度
    private String Location_Y;// 	地理位置经度
    private String Scale;// 	地图缩放大小
    private String Label;// 	地理位置信息
    private String Title;// 	消息标题
    private String Description;// 	消息描述
    private String Url;// 	消息链接
    private String Event;// 	事件类型，subscribe(订阅)、unsubscribe(取消订阅)、LOCATION、CLICK、VIEW
    private String EventKey;// 	事件KEY值
    private String Ticket;// 	二维码的ticket，可用来换取二维码图片
    private String Latitude;// 	地理位置纬度
    private String Longitude;// 	地理位置经度
    private String Precision;// 	地理位置精度
    private String ArticleCount;
    private String MenuId;      // 指菜单ID，如果是个性化菜单，则可以通过这个字段，知道是哪个规则的菜单被点击了

    //订单相关
    private String OrderId;
    private String OrderStatus;
    private String ProductId;
    private String SkuInfo;

    public String getSkuInfo() {
        return SkuInfo;
    }

    public void setSkuInfo(final String skuInfo) {
        SkuInfo = skuInfo;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(final String orderId) {
        OrderId = orderId;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(final String orderStatus) {
        OrderStatus = orderStatus;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(final String productId) {
        ProductId = productId;
    }

    @JacksonXmlProperty(localName="MenuId")
    public String getMenuId() {
        return MenuId;
    }

    @JacksonXmlProperty(localName="MenuId")
    public void setMenuId(final String menuId) {
        MenuId = menuId;
    }

    @JacksonXmlProperty(localName="Event")
    public String getEvent() {
		return Event;
	}

    @JacksonXmlProperty(localName="Event")
	public void setEvent(final String event) {
		Event = event;
	}

    @JacksonXmlProperty(localName="EventKey")
	public String getEventKey() {
		return EventKey;
	}

    @JacksonXmlProperty(localName="EventKey")
	public void setEventKey(final String eventKey) {
		EventKey = eventKey;
	}

	public String getTicket() {
		return Ticket;
	}

	public void setTicket(final String ticket) {
		Ticket = ticket;
	}

	public String getLatitude() {
		return Latitude;
	}

	public void setLatitude(final String latitude) {
		Latitude = latitude;
	}

	public String getLongitude() {
		return Longitude;
	}

	public void setLongitude(final String longitude) {
		Longitude = longitude;
	}

	public String getPrecision() {
		return Precision;
	}

	public void setPrecision(final String precision) {
		Precision = precision;
	}

	public String getFormat() {
		return Format;
	}

	public void setFormat(final String format) {
		Format = format;
	}

	public String getRecognition() {
		return Recognition;
	}

	public void setRecognition(final String recognition) {
		Recognition = recognition;
	}

    @JacksonXmlProperty(localName="PicUrl")
	public String getPicUrl() {
		return PicUrl;
	}

    @JacksonXmlProperty(localName="PicUrl")
	public void setPicUrl(final String picUrl) {
		PicUrl = picUrl;
	}

    @JacksonXmlProperty(localName="MediaId")
	public String getMediaId() {
		return MediaId;
	}

    @JacksonXmlProperty(localName="MediaId")
	public void setMediaId(final String mediaId) {
		MediaId = mediaId;
	}

	public String getThumbMediaId() {
		return ThumbMediaId;
	}

	public void setThumbMediaId(final String thumbMediaId) {
		ThumbMediaId = thumbMediaId;
	}

	public String getLocation_X() {
		return Location_X;
	}

	public void setLocation_X(final String location_X) {
		Location_X = location_X;
	}

	public String getLocation_Y() {
		return Location_Y;
	}

	public void setLocation_Y(final String location_Y) {
		Location_Y = location_Y;
	}

	public String getScale() {
		return Scale;
	}

	public void setScale(final String scale) {
		Scale = scale;
	}

	public String getLabel() {
		return Label;
	}

	public void setLabel(final String label) {
		Label = label;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(final String title) {
		Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(final String description) {
		Description = description;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(final String url) {
		Url = url;
	}

	public String getArticleCount() {
        return ArticleCount;
    }

    public void setArticleCount(final String articleCount) {
        ArticleCount = articleCount;
    }


    @JacksonXmlProperty(localName="ToUserName")
    public String getToUserName() {
        return _toUserName;
    }

    @JacksonXmlProperty(localName="ToUserName")
    public void setToUserName(final String toUserName) {
        _toUserName = toUserName;
    }

    @JacksonXmlProperty(localName="FromUserName")
    public String getFromUserName() {
        return _fromUserName;
    }

    @JacksonXmlProperty(localName="FromUserName")
    public void setFromUserName(final String fromUserName) {
        _fromUserName = fromUserName;
    }

    @JacksonXmlProperty(localName="CreateTime")
    public long getCreateTime() {
        return _createTime;
    }

    @JacksonXmlProperty(localName="CreateTime")
    public void setCreateTime(final long createTime) {
        _createTime = createTime;
    }

    @JacksonXmlProperty(localName="MsgType")
    public String getMsgType() {
        return _msgType;
    }

    @JacksonXmlProperty(localName="MsgType")
    public void setMsgType(final String msgType) {
        _msgType = msgType;
    }

    @JacksonXmlProperty(localName="Content")
    public String getContent() {
        return _content;
    }

    @JacksonXmlProperty(localName="Content")
    public void setContent(final String content) {
        _content = content;
    }

    @JacksonXmlProperty(localName="MsgId")
    public String getMsgId() {
        return _msgId;
    }

    @JacksonXmlProperty(localName="MsgId")
    public void setMsgId(final String msgId) {
        _msgId = msgId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static void main(final String[] args) throws Exception {
        final String xmlsrc =
                "<xml><ToUserName><![CDATA[gh_7fc2a32d2172]]></ToUserName>"
                + "<FromUserName><![CDATA[otw1SwCmNhmAdzARWEZlXzrCmv_w]]></FromUserName>"
                + "<CreateTime>1477579413</CreateTime>"
                + "<MsgType><![CDATA[text]]></MsgType>"
                + "<Content><![CDATA[hi]]></Content>"
                + "<MsgId>6346155256489528442</MsgId>"
                + "</xml>";
        final ObjectMapper mapper = new XmlMapper();

        final FromWXMessage msg = mapper.readValue(xmlsrc, FromWXMessage.class);
        System.out.println("msg:" + msg);
        System.out.println("as Xml:" + mapper.writeValueAsString(msg) );

    }
}
