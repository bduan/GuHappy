package com.guhappy.util;

public class Stock implements Comparable<Stock> {
	private Integer regionindex;
    private String ticker;
    private String name;

    public Stock() {
	super();
    }

    /**
	 * @param regionindex
	 * @param ticker
	 * @param name
	 */
	public Stock(Integer regionindex, String ticker, String name) {
		this.regionindex = regionindex;
		this.ticker = ticker;
		this.name = name;
	}

	public Integer getRegionindex() {
		return regionindex;
	}

	public void setRegionindex(Integer regionindex) {
		this.regionindex = regionindex;
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int compareTo(Stock o) {
	return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
    }
}
