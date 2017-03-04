package org.kane.base.io.snapshot;

import java.util.Objects;

import org.kane.base.examples.Book;
import org.kane.base.immutability.StandardImmutableObject;
import org.kane.base.serialization.Optional;
import org.kane.base.serialization.Validator;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.google.common.collect.ComparisonChain;

public class S3ListRequest extends StandardImmutableObject
{
	private Regions region; // required
	private String bucket_name; // required
	
	private String list_prefix; // optional
	private String start_after; // optional
	private String end_after; // optional
	private int maximum_object_count = Integer.MAX_VALUE;
	
	private S3ListRequest(Builder builder)
	{
	}
	
	public S3ListRequest(Regions region, String bucket_name)
	{
		this.region = region;
		this.bucket_name = bucket_name;
		
		complete();
	}
	
	public Regions getSimpleRegion() { return region; }
	public String getSimpleBucketName() { return bucket_name; }
	
	
	public String getOptionalListPrefix(String default_value) { return Optional.getOptional(list_prefix, null, default_value); }
	public String getOptionalStartAfter(String default_value) { return Optional.getOptional(start_after, null, default_value); }
	public String getOptionalEndAfter(String default_value) { return Optional.getOptional(end_after, null, default_value); }
	public int getSimpleMaximumObjectCount() { return maximum_object_count; }
	
	public boolean hasListPrefix() { return Optional.has(list_prefix, null); }
	public boolean hasStartAfter() { return Optional.has(start_after, null); }
	public boolean hasEndAfter() { return Optional.has(end_after, null); }


	public void freeze()
	{
	}

	public void normalize()
	{
	}

	public void validate()
	{
		Validator.notNull(region);
		Validator.notNull(bucket_name);
	}

	public int hashCode()
	{
		return Objects.hash(getSimpleRegion().getName(),getSimpleBucketName(), getOptionalListPrefix(""), getOptionalStartAfter(""), getOptionalEndAfter(""), getSimpleMaximumObjectCount());
	} 
	
	public int compareTo(Object o)
	{
		if ( !(o instanceof S3ListRequest) ) return 0;
		
		S3ListRequest other = (S3ListRequest)o;
		
		return ComparisonChain.start()
				.compare(getSimpleRegion(), other.getSimpleRegion())
				.compare(getSimpleBucketName(), other.getSimpleBucketName())
				
				.compare(getOptionalListPrefix(""), other.getOptionalListPrefix(""))
				.compare(getOptionalStartAfter(""), other.getOptionalStartAfter(""))
				.compare(getOptionalEndAfter(""), other.getOptionalEndAfter(""))
				
				.compare(getSimpleMaximumObjectCount(), other.getSimpleMaximumObjectCount())
				
				.result();
	}
	
	public boolean equals(Object obj)
	{
		return compareTo(obj) == 0;
	}
	
	static public class Builder 
	{
		private S3ListRequest under_construction;
		
		public Builder(Regions region, String bucket_name)
		{
			under_construction = new S3ListRequest(this);
			setRegion(region);
			setBucketName(bucket_name);
		}
		
		public Builder(S3ListRequest starting_point)
		{
			under_construction = (S3ListRequest)starting_point.deepMutableCloneForBuilder();
		}
		
		public void setRegion(Regions region) { under_construction.region = region; }
		public void setBucketName(String bucket_name) { under_construction.bucket_name = bucket_name; }
		public void setListPrefix(String prefix) { under_construction.list_prefix = prefix; }
		public void setStartAfter(String key) { under_construction.start_after = key; }
		public void setEndAfter(String key) { under_construction.end_after = key; }
		public void setMaximumObjectCount(int count) { under_construction.maximum_object_count = count; }
		
		public S3ListRequest create()
		{
			return (S3ListRequest)under_construction.deepClone();
		}
	}
}
