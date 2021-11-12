package labRetiAssignments.ex08;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class SerializerWrapper
{	
	private static ObjectMapper mapper = null;
	
	private static ObjectMapper getMapper()
	{
		if(mapper == null)
		{
			mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
		}
		return mapper;
	}
	
	public static <T> byte[] serialize(T obj) throws JsonProcessingException
	{
		return getMapper().writeValueAsBytes(obj);		
	}
	
	public static <T> T deserialize(byte[] data, Class<T> data_class) throws StreamReadException, DatabindException, IOException
	{
		return getMapper().readValue(data, data_class);
	}
}