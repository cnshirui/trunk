class Slide < ActionWebService::Struct
  member :name, :string
  member :description,:string
  member :content_num,:string
  member :contents, [Content]
end