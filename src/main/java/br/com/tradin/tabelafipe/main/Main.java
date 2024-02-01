package br.com.tradin.tabelafipe.main;

import br.com.tradin.tabelafipe.model.Data;
import br.com.tradin.tabelafipe.model.Model;
import br.com.tradin.tabelafipe.model.Vehicle;
import br.com.tradin.tabelafipe.service.ApiConsumer;
import br.com.tradin.tabelafipe.service.DataConvert;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    private Scanner leitura = new Scanner(System.in);

    private ApiConsumer apiConsumer = new ApiConsumer();

    private DataConvert dataConvert = new DataConvert();

    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";

        public void showMenu() {

            var menu = """
                    *** OPÇÕES ***
                    Carro
                    Moto
                    Caminhão
                    
                    Digite uma das opções para consultar:
                    """;

            System.out.println(menu);
            var menuOption = leitura.nextLine();
            String address;

            if(menuOption.toLowerCase().contains("carr")){
                address = URL_BASE + "carros/marcas";
            } else if (menuOption.toLowerCase().contains("mot")) {
                address = URL_BASE + "motos/marcas";
            } else {
                address = URL_BASE + "caminhoes/marcas";
            }

            var json = apiConsumer.getData(address);

            System.out.println(json);

            var brands = dataConvert.getListData(json, Data.class);
            brands.stream().sorted(Comparator.comparing(Data::code))
                    .forEach(System.out::println);

            System.out.println("Informe o código da marca para consulta ");
            var brandCode = leitura.nextLine();

            address = address + "/" + brandCode + "/modelos";
            json = apiConsumer.getData(address);

            System.out.println(json);

            var listModel = dataConvert.getData(json, Model.class);

            System.out.println("\nModelos dessa marca: ");
            listModel.models().stream().sorted(Comparator.comparing(Data::code)).forEach(System.out::println);

            System.out.println("\nDigite um trecho do nome do carro a ser buscado: ");
            var vehicleName = leitura.nextLine();

            List<Data> modelsFiltered = listModel.models().stream()
                    .filter(m -> m.name().toLowerCase().contains(vehicleName.toLowerCase()))
                    .collect(Collectors.toList());

            System.out.println("\nModelos filtrados ");
            modelsFiltered.forEach(System.out::println);

            System.out.println("Digite por favor o código do modelo para buscar os valores de avaliação ");
            var modelCode = leitura.nextLine();

            address = address + "/" + modelCode + "/anos";
            json = apiConsumer.getData(address);
            List<Data> years = dataConvert.getListData(json, Data.class);
            List<Vehicle> vehicles = new ArrayList<>();

            for (int i = 0; i < years.size(); i++) {
                var addressYears = address + "/" + years.get(i).code();
                json = apiConsumer.getData(addressYears);
                Vehicle vehicle = dataConvert.getData(json, Vehicle.class);
                vehicles.add(vehicle);
            }

            System.out.println("\nTodos os veìculos filtrados com avaliações por ano: ");
            vehicles.forEach(System.out::println);
            
        }

}
