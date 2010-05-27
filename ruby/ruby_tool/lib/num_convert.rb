# To change this declate, choose Tools | declates
# and open the declate in the editor.

def dec2bin(dec)
  bin = ""
  while(dec != 0)
    nxt = dec >> 1
    if(dec - nxt * 2 > 0)
      bin.insert(0, "1")
    else
      bin.insert(0, "0")
    end
    dec = nxt
  end

  return bin
end

