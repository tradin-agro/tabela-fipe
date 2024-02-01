package br.com.tradin.tabelafipe.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.util.List;

public class DataConvert implements IDataConvert {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T getData(String json, Class<T> classs) {
        try{
            return mapper.readValue(json, classs);
        } catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> List<T> getListData(String json, Class<T> classs) {
        CollectionType listOfData = mapper.getTypeFactory().constructCollectionType(List.class, classs);
        try{
            return mapper.readValue(json, listOfData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
