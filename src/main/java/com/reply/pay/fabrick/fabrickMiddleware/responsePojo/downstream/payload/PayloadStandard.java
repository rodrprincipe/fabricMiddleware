package com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.DownstreamSuccessfulResponsePayload;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayloadStandard extends DownstreamSuccessfulResponsePayload {

    public ArrayList<Object> list;
    public Pagination pagination;
    public Segmentation segmentation;
    public Sorting sorting;

    public class Pagination {
        @JsonProperty("pageCount")
        public int getPageCount() {
            return this.pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        int pageCount;

        @JsonProperty("resultCount")
        public int getResultCount() {
            return this.resultCount;
        }

        public void setResultCount(int resultCount) {
            this.resultCount = resultCount;
        }

        int resultCount;

        @JsonProperty("offset")
        public int getOffset() {
            return this.offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        int offset;

        @JsonProperty("limit")
        public int getLimit() {
            return this.limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        int limit;
    }

    public class Segmentation {
        @JsonProperty("segmentLength")
        public int getSegmentLength() {
            return this.segmentLength;
        }

        public void setSegmentLength(int segmentLength) {
            this.segmentLength = segmentLength;
        }

        int segmentLength;

        @JsonProperty("segmentId")
        public String getSegmentId() {
            return this.segmentId;
        }

        public void setSegmentId(String segmentId) {
            this.segmentId = segmentId;
        }

        String segmentId;

        @JsonProperty("nextSegmentId")
        public String getNextSegmentId() {
            return this.nextSegmentId;
        }

        public void setNextSegmentId(String nextSegmentId) {
            this.nextSegmentId = nextSegmentId;
        }

        String nextSegmentId;
    }

    public class Sorting {
        @JsonProperty("fieldName")
        public String getFieldName() {
            return this.fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        String fieldName;

        @JsonProperty("type")
        public String getType() {
            return this.type;
        }

        public void setType(String type) {
            this.type = type;
        }

        String type;
    }
}