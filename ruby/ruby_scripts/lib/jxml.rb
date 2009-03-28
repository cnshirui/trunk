require 'java'

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathFactory
import javax.xml.xpath.XPathConstants

import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

import org.xml.sax.InputSource

import java.io.StringReader
import java.io.StringWriter

module JXML

  #
  # Node represents a single node in the document tree
  #
  class Node

    #
    # return the name of this node, for details please refer to
    # http://java.sun.com/j2se/1.5.0/docs/api/org/w3c/dom/Node.html#getNodeName()
    #
    def name
      @instance.getNodeName
    end

    #
    # search nodes based on the xpath, return the first node as Element if the 
    # range's value is :first, else return all the matching nodes as NodeList
    #
    def find xpath, range = :all
      @xpath ||= XPathFactory.newInstance.newXPath
      
      node_list = NodeList.new(@xpath.compile(xpath).evaluate(@instance, XPathConstants::NODESET))
      range == :first ? node_list.first : node_list
    end

    #
    # search nodes based on tag name, it uses getElementByTagName method 
    # which is faster than xpath searching; return the first node if the 
    # range's value is :first, else return all the matching nodes
    #
    def find_by_tag_name tag_name, range = :all
      node_list = NodeList.new(@instance.getElementsByTagName(tag_name))
      range == :first ? node_list.first : node_list
    end

    #
    # return the text content of this node
    #
    def value
      @instance.getTextContent
    end

    #
    # set the text content of this node
    #
    def value= new_value
      @instance.setTextContent new_value
    end
    
    #
    # return the current node as xml string
    #
    def to_xml
      @transformer ||= TransformerFactory.newInstance.newTransformer
      @dom_source ||= DOMSource.new @instance
      
      string_writer = StringWriter.new
      stream_result = StreamResult.new(string_writer)
      
      @transformer.transform @dom_source, stream_result
      string_writer.toString
    end

    #
    # return the parent of this node
    #
    def parent
      @parent ||= Element.create @instance.getParentNode
    end

    #
    # return child index in it's parent
    #
    def child_index
      if(parent.hash == @instance.getOwnerDocument.hashCode)
        return -1
      else
        i = 0
        parent.children.each do |node|
          return i if(node==self)
          i += 1
        end
      end
    end



    #
    # return whether this node has any children
    #
    def has_children?
      @instance.hasChildNodes
    end

    #
    # return a NodeList that contains all children of this node, if
    # there are no children, this is a NodeList containing no nodes
    #
    def children
      @children ||= NodeList.new @instance.getChildNodes
    end

    #
    # return the first child of this node
    #
    def first_child
      self.children
      @children.first
    end

    #
    # return the last child of this node
    #
    def last_child
      self.children
      @children.last
    end

    #
    # return the node immediately preceding this node
    # if there is no such node, return nil
    #
    def previous_sibling
      @previous_sibling ||= Element.create @instance.getPreviousSibling
    end

    #
    # return the node immediately following this node
    # if there is no such node, return nil
    #
    def next_sibling
      @next_sibling ||= Element.create @instance.getNextSibling
    end

    #
    # Adds the node new_child to the end of the list of children of this node. 
    # If the new_child is already in the tree, it is first removed. 
    #
    def append_child new_child
      @instance.appendChild new_child.instance
    end

    def instance
      @instance
    end
    
    # default deep clone
    def copy(deep = true)
      return Element.create(@instance.cloneNode(deep))
    end
    
    # remove a child node
    def remove_child node
      @instance.removeChild(node.instance) 
    end
#    
#    def getParentNode
#      return @instance.getParentNode
#    end

  end

  #
  # NodeList represents an ordered collection of nodes, items in the
  # NodeList are accessible via an integral index, starting from 0.
  #
  class NodeList
    include Enumerable

    def initialize node_list
      @node_list = node_list
      @nodes = {}
    end

    #
    # implement each method so that it could make use of methods in Enumberable module
    #
    def each
      0.upto(self.length-1) do |i|
        yield self[i]
      end
    end

    def empty?
      self.length == 0
    end

    def length
      @node_list ? @node_list.getLength : 0
    end
    alias_method :size, :length

    def [] index
      return nil if @node_list.nil?
      raise "index (#{index}) must be integer" unless index.kind_of? Integer

      if @nodes.has_key? index
        @nodes[index] 
      else
        node = @node_list.item(index)
        return nil if node.nil?
        # cache this item
        @nodes[index] = Element.create(node)
      end
    end

    #
    # return the first node of the node list
    #
    def first
      self[0]
    end

    #
    # return the last node of the node list
    #
    def last
      self[self.length-1]
    end

  end

  #
  # represent the entire XML or HTML document, it is the root of the document tree
  #
  class Document < Node

    def initialize xml
      doc_factory = DocumentBuilderFactory.newInstance
      doc_builder = doc_factory.newDocumentBuilder
      input_source = InputSource.new(StringReader.new(xml))
      @instance = doc_builder.parse input_source
    end

    def create_element tag_name
      Element.create(@instance.createElement(tag_name))
    end

    def get_root_element
      return Element.create(@instance.getDocumentElement)
    end

    def import_node(node)
      return Element.create(@instance.importNode(node.instance, true))
    end
  end

  #
  # represent an element in an XML or HTML document
  #
  class Element < Node

    def initialize element
      @instance = element
    end

    def self.create element
      element ? new(element) : nil
    end

    def eql? other
      self.hash == other.hash
    end

    def == other
      self.eql? other
    end

    def hash
      @instance.hashCode
    end

    #
    # return the element attribute value based on the attribute name
    #
    # TODO-AZ: Implement attribute class
    def attributes name
      @instance.getAttribute name
    end

    def set_attribute name, value
      @instance.setAttribute name, value
    end
  end

end
