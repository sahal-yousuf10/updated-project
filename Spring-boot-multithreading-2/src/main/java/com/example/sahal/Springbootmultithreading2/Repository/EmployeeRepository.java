package com.example.sahal.Springbootmultithreading2.Repository;

import com.example.sahal.Springbootmultithreading2.Model.Employee;
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{

    List<Employee> findAllByCityId(long id);
    List<Employee> findAllByCompanyId(long id);
    Optional<Employee> findByRegistrationId(long id);
    @Query(value = "DELETE * FROM employee WHERE first_name LIKE test%" , nativeQuery = true)
    void deleteTestData();
}
