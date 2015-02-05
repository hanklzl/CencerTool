package com.zoneol.censortool;
import com.zoneol.lovebirds.protocol.JoyProtocol.BbsTopic;
public class TopBbsInfo {
	private BbsTopic bbs;
	private int hits;
	private int topicId;
	private int topicType;
	private String title;
	private String content;
	private long publishTime;
	private long publicshUid;
	private String publishNickName;
	private int publisImgId;
	private int publishImgSex;
	private int hideUid;
	private int imageId ;
	private String imageUrl ;
	public BbsTopic getBbs() {
		return bbs;
	}

	public void setBbs(BbsTopic bbs) {
		this.bbs = bbs;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}
	
	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	public int getTopicType() {
		return topicType;
	}

	public void setTopicType(int topicType) {
		this.topicType = topicType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(long publishTime) {
		this.publishTime = publishTime;
	}

	public long getPublicshUid() {
		return publicshUid;
	}

	public void setPublicshUid(long publicshUid) {
		this.publicshUid = publicshUid;
	}

	public String getPublishNickName() {
		return publishNickName;
	}

	public void setPublishNickName(String publishNickName) {
		this.publishNickName = publishNickName;
	}

	public int getPublisImgId() {
		return publisImgId;
	}

	public void setPublisImgId(int publisImgId) {
		this.publisImgId = publisImgId;
	}

	public int getPublishImgSex() {
		return publishImgSex;
	}

	public void setPublishImgSex(int publishImgSex) {
		this.publishImgSex = publishImgSex;
	}

	public int getHideUid() {
		return hideUid;
	}

	public void setHideUid(int hideUid) {
		this.hideUid = hideUid;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		return bbs.content;
	}

	
}
