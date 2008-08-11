__author__ = 'Ping Chen'


from paginator import \
    Paginator, QuerySetPaginator, Page, InvalidPage

adjacent_pages = 10
page_offset = 2

class GqlQueryPaginator(Paginator):
    """
    Like Paginator, but works on google GqlQuery.
    Query the data for each page to resolve the performance issue of mass records.
    """
    def __init__(self, gqlQuery,page_number,per_page, orphans=0, allow_empty_first_page=True):
        self.gqlQuery = gqlQuery
        self.number = page_number
        bottom = (self.number - 1) * per_page
        self.object_list = gqlQuery.fetch(per_page,bottom)
        Paginator.__init__(self, self.object_list, per_page, orphans, allow_empty_first_page)

    def page(self):
        "Returns a Page object for the given 1-based page number."
        number = self.validate_number(self.number)
        return GqlPage(self.object_list, number, self)

    def _get_count(self):
        "Returns the total number of objects, across all pages."
        if self._count is None:
            self._count =self.gqlQuery.count() ##todo:google GqlQuery has 1000 records limit.
        return self._count
    count = property(_get_count)


class GqlPage(Page):

    def page_numbers(self):
        return range(self.from_to_page()["from_page"],self.from_to_page()["to_page"])

    def show_first(self):
        return   self.paginator.number - page_offset > 1 and self.real_pages() > adjacent_pages  

    def show_last(self):
        return  self.from_to_page()["to_page"] < self.real_pages()

    def has_next(self):
        return  self.from_to_page()["to_page"] < self.real_pages()

    def has_previous(self):
        return  self.paginator.number>1

    def real_pages(self):
        return   self.paginator.count//self.paginator.per_page +1

    def from_to_page(self):
         if(adjacent_pages> self.real_pages()):
            from_page = 1
            to_page = self.real_pages()    
         else :
            from_page = self.paginator.number - page_offset
            to_page = from_page + adjacent_pages
            if from_page <1:
                to_page = from_page + adjacent_pages
                from_page = 1
                if to_page - from_page < adjacent_pages :
                    to_page = adjacent_pages
            else :
                if  to_page > self.real_pages():
                    from_page = self.real_pages() - adjacent_pages +1
                    to_page = self.real_pages()
         return {"from_page":from_page,"to_page":to_page}
         
