package net.beans;

import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import net.db.MyUtil;
import net.db.model.Contact;
import org.apache.ibatis.session.SqlSession;

@ManagedBean
@SessionScoped
public class ContactBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private Contact contact;
	private List<Contact> filteredContact;
	
	public List<Contact> getFilteredContact() {
		return filteredContact;
	}
	public void setFilteredContact(List<Contact> filteredContact) {
		this.filteredContact = filteredContact;
	}
	public ContactBean() {
		contact = new Contact();
	}
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	public void clean() {
		contact.setId(0);
		contact.setName(null);
		contact.setAge(0);
	}
	public String saveContact(Contact contact){
		SqlSession session = new MyUtil().getSession();
		String msg = null;
		if(session != null){
			if(contact.getId() > 0) {
				session.update("Contact.updContact", contact);
				msg = "Updated Contact";
			} else {
				contact.setId(-1);
				session.insert("Contact.addContact", contact);
				msg = "Created Contact";
			}
		session.close();
		}
		FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", msg));
		return "index";
	}
	/*public Contact searchById(int id) {
		Contact one = null;
		SqlSession session = new MyUtil().getSession();
		if(session != null){
			one = session.selectOne("Contact.searchById", id);
			session.close();
		}
		return one;
	}*/
	public void editContact(Contact contact) {
		this.setContact(contact);
	}
	public String delContact(int id) {
		SqlSession session = new MyUtil().getSession();
		if(session != null){
			session.delete("Contact.delContact", id);
			session.commit();
			session.close();
		}
		FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Deleted Contact"));
		return "index";
	}
	public List<Contact> getContacts(){
		SqlSession session = new MyUtil().getSession();
		List<Contact> list = null;
		if(session != null){
		list = session.selectList("Contact.getContacts");
		session.close();
		}
		return list;
	}
}