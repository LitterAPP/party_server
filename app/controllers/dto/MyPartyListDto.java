package controllers.dto;

import java.util.ArrayList;
import java.util.List;

public class MyPartyListDto {
	
	private List<ApplyInfoItemDto> items = new ArrayList<ApplyInfoItemDto>();
	private int count ;
	
	public List<ApplyInfoItemDto> getItems() {
		return items;
	}
	public void setItems(List<ApplyInfoItemDto> items) {
		this.items = items;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
