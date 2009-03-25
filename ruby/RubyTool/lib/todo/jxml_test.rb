
require File.dirname(__FILE__) + '/../test_helper'

class TestJXML < Test::Unit::TestCase
  def test_document
    xml = "<person><name>James</name><phone>1234</phone><phone>5678</phone></person>"
    document = JXML::Document.new xml
    assert_not_nil(document)

    phone_number = document.find("//phone")
    assert_not_nil(phone_number)
    assert_equal(2, phone_number.length)

    address = document.find("//address")
    assert_not_nil(address)
    assert_equal(0, address.length)

    name = document.find_by_tag_name("name")
    assert_not_nil(name)
    assert_equal(1, name.length)
  end
  
  def test_attribute
    xml = "<person gender='man'>Tom</person>"
    document = JXML::Document.new xml
    assert_not_nil(document)

    person = document.find_by_tag_name("person", :first)
    assert_not_nil(person)
    assert_equal("man", person.attributes("gender"))
    assert_equal("", person.attributes("company"))

    person.set_attribute "gender", "woman"
    assert_equal("woman", person.attributes("gender"))
  end
  
  def test_element
    xml = "<person><name>James</name><age>25</age></person>"
    document = JXML::Document.new xml
    assert_not_nil(document)

    assert_nil JXML::Element.create(nil)

    person = document.find_by_tag_name("person", :first)
    assert_not_nil person
    name = person.find_by_tag_name("name", :first)
    assert_not_nil name
    assert_equal("James", name.value)

    age = document.find("/person/age", :first)
    assert_not_nil age
    assert_equal("25", age.value)

    age.value = "26"
    assert_equal("26", age.value)

    age1 = document.find_by_tag_name("age", :first)
    age2 = document.find_by_tag_name("age", :first)

    assert_equal(true, age1.eql?(age2))
    assert_equal(true, age1 == age2)
  end

  def test_xpath_parse
    xml = "<r><p a='1' b='2'>1</p><p a='2' b='1'>2</p></r>"
    document = JXML::Document.new xml
    assert_not_nil(document)
    
    p1 = document.find("//p[@a='1']", :first)
    assert_not_nil(p1)
    assert_equal("1", p1.value)
    
    p2 = document.find("//p[@a='2'][@b='1']", :first)
    assert_not_nil(p2)
    assert_equal("2", p2.value)
    
    p3 = document.find("//p[@a='1'][@b='1']", :first)
    assert_nil p3
  end

  def test_to_xml
    header1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
    header2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
    
    xml = "<person><name>James</name><age>25</age></person>"
    document = JXML::Document.new xml
    assert_equal header1+xml, document.to_xml
    
    age = document.find "//age", :first
    assert_not_nil age
    assert_equal header2+"<age>25</age>", age.to_xml
  end
  
  def test_node_list
    xml = "<r><p>1</p><p>2</p></r>"
    doc = JXML::Document.new xml
    assert_not_nil(doc)
    
    nodes = doc.find("//p")
    assert_equal(false, nodes.empty?)
    assert_equal(2, nodes.length)
    
    values = nodes.collect { |p| p.value }
    assert_equal(["1","2"], values)

    assert_not_nil nodes.first
    assert_not_nil nodes[1]
    assert_nil nodes[2]
    assert_equal(true, nodes[0] == nodes[0])
  end

  def test_node_children_and_sibling
    xml = "<a><b><c/><d/><e/></b><f/></a>"
    doc = JXML::Document.new xml
    assert_not_nil(doc)

    a = doc.find_by_tag_name "a", :first
    assert_not_nil(a)

    b = doc.find_by_tag_name "b", :first
    assert_not_nil(b)

    c = doc.find_by_tag_name "c", :first
    assert_not_nil(c)
    
    e = doc.find_by_tag_name "e", :first
    assert_not_nil(e)

    assert_equal a, b.parent
    assert_equal true, b.has_children?
    assert_equal b.first_child, c
    assert_not_equal b.first_child.object_id, c.object_id

    assert_equal b.last_child, e

    f = doc.find_by_tag_name "f", :first
    assert_not_nil(f)

    assert_equal f, b.next_sibling
    assert_equal b.next_sibling.object_id, b.next_sibling.object_id
    assert_equal nil, b.previous_sibling
    assert_equal nil, f.next_sibling
  end

  def test_append_child
    header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
    xml  = "<a><b/></a>"
    doc = JXML::Document.new xml
    assert_not_nil doc

    a = doc.find_by_tag_name "a", :first
    assert_not_nil a

    c = doc.create_element "c"
    assert_not_nil c

    a.append_child c
    assert_equal header+"<a><b/><c/></a>", a.to_xml
    # If the new child is already in the tree, it is first removed. 
    a.append_child c
    assert_equal header+"<a><b/><c/></a>", a.to_xml

    d1 = doc.create_element "d"
    assert_not_nil d1
    d1.value = "D1"

    a.append_child d1
    assert_equal header+"<a><b/><c/><d>D1</d></a>", a.to_xml

    d2 = doc.create_element "d"
    assert_not_nil d2
    d2.value = "D2"
    d2.set_attribute "id", "2"

    a.append_child d2
    assert_equal header+"<a><b/><c/><d>D1</d><d id=\"2\">D2</d></a>", a.to_xml
  end
end