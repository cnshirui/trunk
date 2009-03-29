Gem::Specification.new do |s|
  s.name = %q{activesupport}
  s.version = "2.0.2"

  s.specification_version = 2 if s.respond_to? :specification_version=

  s.required_rubygems_version = Gem::Requirement.new(">= 0") if s.respond_to? :required_rubygems_version=
  s.authors = ["David Heinemeier Hansson"]
  s.date = %q{2007-12-20}
  s.description = %q{Utility library which carries commonly used classes and goodies from the Rails framework}
  s.email = %q{david@loudthinking.com}
  s.files = ["CHANGELOG", "README", "lib/active_support", "lib/active_support/basic_object.rb", "lib/active_support/buffered_logger.rb", "lib/active_support/clean_logger.rb", "lib/active_support/core_ext", "lib/active_support/core_ext/array", "lib/active_support/core_ext/array/access.rb", "lib/active_support/core_ext/array/conversions.rb", "lib/active_support/core_ext/array/extract_options.rb", "lib/active_support/core_ext/array/grouping.rb", "lib/active_support/core_ext/array/random_access.rb", "lib/active_support/core_ext/array.rb", "lib/active_support/core_ext/bigdecimal", "lib/active_support/core_ext/bigdecimal/conversions.rb", "lib/active_support/core_ext/bigdecimal.rb", "lib/active_support/core_ext/blank.rb", "lib/active_support/core_ext/cgi", "lib/active_support/core_ext/cgi/escape_skipping_slashes.rb", "lib/active_support/core_ext/cgi.rb", "lib/active_support/core_ext/class", "lib/active_support/core_ext/class/attribute_accessors.rb", "lib/active_support/core_ext/class/delegating_attributes.rb", "lib/active_support/core_ext/class/inheritable_attributes.rb", "lib/active_support/core_ext/class/removal.rb", "lib/active_support/core_ext/class.rb", "lib/active_support/core_ext/date", "lib/active_support/core_ext/date/behavior.rb", "lib/active_support/core_ext/date/calculations.rb", "lib/active_support/core_ext/date/conversions.rb", "lib/active_support/core_ext/date.rb", "lib/active_support/core_ext/date_time", "lib/active_support/core_ext/date_time/calculations.rb", "lib/active_support/core_ext/date_time/conversions.rb", "lib/active_support/core_ext/date_time.rb", "lib/active_support/core_ext/duplicable.rb", "lib/active_support/core_ext/enumerable.rb", "lib/active_support/core_ext/exception.rb", "lib/active_support/core_ext/file.rb", "lib/active_support/core_ext/float", "lib/active_support/core_ext/float/rounding.rb", "lib/active_support/core_ext/float.rb", "lib/active_support/core_ext/hash", "lib/active_support/core_ext/hash/conversions.rb", "lib/active_support/core_ext/hash/diff.rb", "lib/active_support/core_ext/hash/except.rb", "lib/active_support/core_ext/hash/indifferent_access.rb", "lib/active_support/core_ext/hash/keys.rb", "lib/active_support/core_ext/hash/reverse_merge.rb", "lib/active_support/core_ext/hash/slice.rb", "lib/active_support/core_ext/hash.rb", "lib/active_support/core_ext/integer", "lib/active_support/core_ext/integer/even_odd.rb", "lib/active_support/core_ext/integer/inflections.rb", "lib/active_support/core_ext/integer.rb", "lib/active_support/core_ext/kernel", "lib/active_support/core_ext/kernel/agnostics.rb", "lib/active_support/core_ext/kernel/daemonizing.rb", "lib/active_support/core_ext/kernel/debugger.rb", "lib/active_support/core_ext/kernel/reporting.rb", "lib/active_support/core_ext/kernel/requires.rb", "lib/active_support/core_ext/kernel.rb", "lib/active_support/core_ext/load_error.rb", "lib/active_support/core_ext/logger.rb", "lib/active_support/core_ext/module", "lib/active_support/core_ext/module/aliasing.rb", "lib/active_support/core_ext/module/attr_accessor_with_default.rb", "lib/active_support/core_ext/module/attr_internal.rb", "lib/active_support/core_ext/module/attribute_accessors.rb", "lib/active_support/core_ext/module/delegation.rb", "lib/active_support/core_ext/module/inclusion.rb", "lib/active_support/core_ext/module/introspection.rb", "lib/active_support/core_ext/module/loading.rb", "lib/active_support/core_ext/module.rb", "lib/active_support/core_ext/name_error.rb", "lib/active_support/core_ext/numeric", "lib/active_support/core_ext/numeric/bytes.rb", "lib/active_support/core_ext/numeric/time.rb", "lib/active_support/core_ext/numeric.rb", "lib/active_support/core_ext/object", "lib/active_support/core_ext/object/conversions.rb", "lib/active_support/core_ext/object/extending.rb", "lib/active_support/core_ext/object/instance_variables.rb", "lib/active_support/core_ext/object/misc.rb", "lib/active_support/core_ext/object.rb", "lib/active_support/core_ext/pathname", "lib/active_support/core_ext/pathname/clean_within.rb", "lib/active_support/core_ext/pathname.rb", "lib/active_support/core_ext/proc.rb", "lib/active_support/core_ext/range", "lib/active_support/core_ext/range/blockless_step.rb", "lib/active_support/core_ext/range/conversions.rb", "lib/active_support/core_ext/range/include_range.rb", "lib/active_support/core_ext/range/overlaps.rb", "lib/active_support/core_ext/range.rb", "lib/active_support/core_ext/string", "lib/active_support/core_ext/string/access.rb", "lib/active_support/core_ext/string/conversions.rb", "lib/active_support/core_ext/string/inflections.rb", "lib/active_support/core_ext/string/iterators.rb", "lib/active_support/core_ext/string/starts_ends_with.rb", "lib/active_support/core_ext/string/unicode.rb", "lib/active_support/core_ext/string/xchar.rb", "lib/active_support/core_ext/string.rb", "lib/active_support/core_ext/symbol.rb", "lib/active_support/core_ext/test", "lib/active_support/core_ext/test/unit", "lib/active_support/core_ext/test/unit/assertions.rb", "lib/active_support/core_ext/test.rb", "lib/active_support/core_ext/time", "lib/active_support/core_ext/time/behavior.rb", "lib/active_support/core_ext/time/calculations.rb", "lib/active_support/core_ext/time/conversions.rb", "lib/active_support/core_ext/time.rb", "lib/active_support/core_ext.rb", "lib/active_support/dependencies.rb", "lib/active_support/deprecation.rb", "lib/active_support/duration.rb", "lib/active_support/inflections.rb", "lib/active_support/inflector.rb", "lib/active_support/json", "lib/active_support/json/decoding.rb", "lib/active_support/json/encoders", "lib/active_support/json/encoders/date.rb", "lib/active_support/json/encoders/date_time.rb", "lib/active_support/json/encoders/enumerable.rb", "lib/active_support/json/encoders/false_class.rb", "lib/active_support/json/encoders/hash.rb", "lib/active_support/json/encoders/nil_class.rb", "lib/active_support/json/encoders/numeric.rb", "lib/active_support/json/encoders/object.rb", "lib/active_support/json/encoders/regexp.rb", "lib/active_support/json/encoders/string.rb", "lib/active_support/json/encoders/symbol.rb", "lib/active_support/json/encoders/time.rb", "lib/active_support/json/encoders/true_class.rb", "lib/active_support/json/encoding.rb", "lib/active_support/json/variable.rb", "lib/active_support/json.rb", "lib/active_support/multibyte", "lib/active_support/multibyte/chars.rb", "lib/active_support/multibyte/generators", "lib/active_support/multibyte/generators/generate_tables.rb", "lib/active_support/multibyte/handlers", "lib/active_support/multibyte/handlers/passthru_handler.rb", "lib/active_support/multibyte/handlers/utf8_handler.rb", "lib/active_support/multibyte/handlers/utf8_handler_proc.rb", "lib/active_support/multibyte.rb", "lib/active_support/option_merger.rb", "lib/active_support/ordered_options.rb", "lib/active_support/test_case.rb", "lib/active_support/testing", "lib/active_support/testing/default.rb", "lib/active_support/testing.rb", "lib/active_support/values", "lib/active_support/values/time_zone.rb", "lib/active_support/values/unicode_tables.dat", "lib/active_support/vendor", "lib/active_support/vendor/builder-2.1.2", "lib/active_support/vendor/builder-2.1.2/blankslate.rb", "lib/active_support/vendor/builder-2.1.2/builder", "lib/active_support/vendor/builder-2.1.2/builder/blankslate.rb", "lib/active_support/vendor/builder-2.1.2/builder/css.rb", "lib/active_support/vendor/builder-2.1.2/builder/xchar.rb", "lib/active_support/vendor/builder-2.1.2/builder/xmlbase.rb", "lib/active_support/vendor/builder-2.1.2/builder/xmlevents.rb", "lib/active_support/vendor/builder-2.1.2/builder/xmlmarkup.rb", "lib/active_support/vendor/builder-2.1.2/builder.rb", "lib/active_support/vendor/xml-simple-1.0.11", "lib/active_support/vendor/xml-simple-1.0.11/xmlsimple.rb", "lib/active_support/vendor.rb", "lib/active_support/version.rb", "lib/active_support/whiny_nil.rb", "lib/active_support.rb", "lib/activesupport.rb"]
  s.has_rdoc = true
  s.homepage = %q{http://www.rubyonrails.org}
  s.require_paths = ["lib"]
  s.rubyforge_project = %q{activesupport}
  s.rubygems_version = %q{1.0.1}
  s.summary = %q{Support and utility classes used by the Rails framework.}
end