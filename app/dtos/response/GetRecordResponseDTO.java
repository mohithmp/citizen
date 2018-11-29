package dtos.response;

import java.util.ArrayList;
import java.util.List;

import models.Record;

public class GetRecordResponseDTO {

	public List<Record> records = new ArrayList<Record>();

	public long totalCount = 0;

}
