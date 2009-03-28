class Presentation < ActionWebService::Struct
  member :title, :string
  member :submitter, :string
  member :slide_num, :string
  member :slides, [Slide]
end