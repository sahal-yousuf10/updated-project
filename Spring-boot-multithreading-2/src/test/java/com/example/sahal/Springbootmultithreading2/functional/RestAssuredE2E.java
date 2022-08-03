package com.example.sahal.Springbootmultithreading2.functional;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.json.simple.JSONObject;
import java.io.File;

@Slf4j
public class RestAssuredE2E {

    @Test
    public void testAddEmployee(){
            JSONObject request = new JSONObject();
            request.put("firstName", "Haristest");
            request.put("lastName", "Qadeer");
            request.put("email", "haris@gmail.com");
            request.put("gender", "Male");
            request.put("companyId", 2);
            request.put("jobTitle", "Senior Editor");
            request.put("registrationId", 6007);
            request.put("cityId", 3);

            RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .body(request.toJSONString()) // try to get this body from a file (file.json)
                    .when()
                    .post("http://localhost:8098/employee")
                    .then()
                    .statusCode(201);
    }

    @Test
    public void testAddEmployeesThroughFile(){

        File testFile = new File("/Users/msahal/Documents/workspace/Record.txt");
        RestAssured
                .given()
                .multiPart("files", testFile)
                .contentType(ContentType.MULTIPART)
                .when()
                .post("http://localhost:8098/employees")
                .then()
                .statusCode(201);
    }

    @Test
    public void testUpdateEmployee(){
        JSONObject request = new JSONObject();
        request.put("id", "6005");
        request.put("firstName", "Faraztest");
        request.put("lastName", "Qadeer");
        request.put("email", "haris@gmail.com");
        request.put("gender", "Male");
        request.put("companyId", 2);
        request.put("jobTitle", "Senior Editor");
        request.put("registrationId", 15444);
        request.put("cityId", 3);

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(request.toJSONString()) // try to get this body from a file (file.json)
                .when()
                .put("http://localhost:8098/employee/6005")
                .then()
                .statusCode(201);
    }

    @Test
    public void  testGetEmployee(){

        ExtractableResponse<Response> response = RestAssured
                .given()
                .when()
                .get("http://localhost:8098/employee/6005")
                .then()
                .statusCode(302)
                .extract();

        JsonPath jp = new JsonPath(response.asString());
        log.info(response.toString());
        Assert.assertEquals("6005", jp.get("employeeList.id[0]").toString());
        Assert.assertEquals("Faraztest", jp.get("employeeList.firstName[0]").toString());
        Assert.assertEquals("Qadeer", jp.get("employeeList.lastName[0]").toString());
        Assert.assertEquals("haris@gmail.com", jp.get("employeeList.email[0]").toString());
        Assert.assertEquals("Male", jp.get("employeeList.gender[0]").toString());
        Assert.assertEquals("2", jp.get("employeeList.companyId[0]").toString());
        Assert.assertEquals("Senior Editor", jp.get("employeeList.jobTitle[0]").toString());
        Assert.assertEquals("15444", jp.get("employeeList.registrationId[0]").toString());
        Assert.assertEquals("3", jp.get("employeeList.cityId[0]").toString());
        Assert.assertEquals("Nisum", jp.get("company.name").toString());
        Assert.assertEquals("2", jp.get("company.id").toString());
        Assert.assertEquals("Islamabad", jp.get("city.name").toString());
        Assert.assertEquals("3", jp.get("city.id").toString());
    }

    @Test
    public void  testGetAllEmployees(){

        RestAssured
                .given()
                .when()
                .get("http://localhost:8098/employees")
                .then()
                .body("id[7]", Matchers.notNullValue())
                .body("firstName[7]", Matchers.equalTo("Howie"))
                .body("lastName[7]", Matchers.equalTo("MacNeilly"))
                .body("email[7]", Matchers.equalTo("hmacneilly2@google.com.br"))
                .body("gender[7]", Matchers.equalTo("Male"))
                .body("companyId[7]", Matchers.equalTo(8))
                .body("jobTitle[7]", Matchers.equalTo("Quality Control Specialist"))
                .body("registrationId[7]", Matchers.equalTo(3199))
                .body("cityId[7]", Matchers.equalTo(5));
    }

    @Test
    public void  testGetAllEmployeesByCity(){

        ExtractableResponse<Response> response = RestAssured
                .given()
                .when()
                .get("http://localhost:8098/employees/city/karachi")
                .then()
                .statusCode(302)
                .extract();

        JsonPath jp = new JsonPath(response.asString());
        log.info(response.toString());
        Assert.assertEquals("5", jp.get("employeeList.id[0]").toString());
        Assert.assertEquals("Bax", jp.get("employeeList.firstName[0]").toString());
        Assert.assertEquals("Barlace", jp.get("employeeList.lastName[0]").toString());
        Assert.assertEquals("bbarlace1@plala.or.jp", jp.get("employeeList.email[0]").toString());
        Assert.assertEquals("Karachi", jp.get("city.name").toString());
        Assert.assertEquals("2", jp.get("city.id").toString());
    }

    @Test
    public void  testGetAllEmployeesByCompany(){

        ExtractableResponse<Response> response = RestAssured
                .given()
                .when()
                .get("http://localhost:8098/employees/company/nisum")
                .then()
                .statusCode(302)
                .extract();

        JsonPath jp = new JsonPath(response.asString());
        Assert.assertEquals("6", jp.get("employeeList.id[0]").toString());
        Assert.assertEquals("Ingemar", jp.get("employeeList.firstName[0]").toString());
        Assert.assertEquals("Werlock", jp.get("employeeList.lastName[0]").toString());
        Assert.assertEquals("iwerlock1@diigo.com", jp.get("employeeList.email[0]").toString());
        Assert.assertEquals("Nisum", jp.get("company.name").toString());
        Assert.assertEquals("2", jp.get("company.id").toString());
    }

    @Test
    public void DeleteEmployee(){

        RestAssured
                .given()
                .when()
                .delete("http://localhost:8098/employee/6004")
                .then()
                .statusCode(200);
    }
}
