str = "CX!R1C14"
sheet_name, row_col = str.split(/!R/)
row, col = row_col.split(/C/)
puts sheet_name, row, col
