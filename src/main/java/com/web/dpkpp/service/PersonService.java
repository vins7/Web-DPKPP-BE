package com.web.dpkpp.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.zip.DeflaterOutputStream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.web.dpkpp.dao.PersonDao;
import com.web.dpkpp.model.Person;
import com.web.dpkpp.model.Unit;

@Service
@Transactional
public class PersonService extends BaseService  {

	@Autowired
	private PersonDao personDao;
	
	public void save(Person person) throws Exception {
		try {
			if(personDao.getPersonByNip(person.getNip())!=null) {	
				throw new Exception("Person is exist !") ;
			}else {
				personDao.save(person);
			}
		} catch (Exception e) {
			throw e;	
		}	
	}
	
	public void edit(MultipartFile file,String persons) throws Exception{
		Person person = new Person();
	   
		person = super.readValue(persons, Person.class);
		byte[] bytes = Base64.getEncoder().encode(file.getBytes());
		String photo = Base64.getEncoder().encodeToString(bytes);
		person.setPhoto(photo);
		person.setTypeFile(file.getContentType());
		person.setFileName(file.getOriginalFilename());
		try {
			Person tempPerson = personDao.getPersonById(person.getId());
			if(tempPerson!=null) {
				Unit unit = person.getUnit();
				tempPerson.setName(person.getName());
				tempPerson.setPhoto(person.getPhoto());
				tempPerson.setPosition(person.getPosition());
				tempPerson.setTypeFile(person.getTypeFile());
				tempPerson.setGender(person.getGender());
				tempPerson.setFileName(person.getFileName());
				tempPerson.setUnit(unit);
				personDao.edit(tempPerson);
			}
		} catch (Exception e) {
			throw e;
		}
	}
}
