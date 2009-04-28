pid = fork {
  # child
  sleep 3
}

th = Process.detach(pid)
p th.value
