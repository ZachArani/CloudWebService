package edu.ou.oudb.cloudwebservice.entities;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class Cost implements Serializable {


	private static final long serialVersionUID = 1L;
	
	private long mTime;
	private double mMoney;
	
	public Cost()
	{
		mTime = 0;
		mMoney = 0;
	}
	
	public Cost(long time, double money)
	{
		this.mTime = time;
		this.mMoney = money;
	}
	
	public long getTime() {
		return mTime;
	}
	
	public void setTime(long time) {
		this.mTime = time;
	}
	public double getMoney() {
		return mMoney;
	}
	public void setMoney(double money) {
		this.mMoney = money;
	}
	
}
