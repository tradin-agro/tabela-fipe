package br.com.tradin.tabelafipe.service;

import java.util.List;

public interface IDataConvert {
    <T> T getData(String json, Class<T> classs);

    <T> List<T> getListData(String json, Class<T> classs);
}
