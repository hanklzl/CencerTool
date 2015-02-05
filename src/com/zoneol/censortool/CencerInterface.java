package com.zoneol.censortool;

public interface CencerInterface {
	public void moveToCategory(int pos,int categoryNo);
	public void deleteSecret(int pos);
    public void passSecret(int pos);
    public void getTopicCategory();
}
