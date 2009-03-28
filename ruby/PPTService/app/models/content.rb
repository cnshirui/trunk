class Content < ActionWebService::Struct
  member :type, :string
  member :source_num, :string
  member :sources, [:string]
end