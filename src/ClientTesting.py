import asyncore, socket

class ClientTest(asyncore.dispatcher):

	def __init__(self, host, port):
		asyncore.dispatcher.__init__(self)
		self.create_socket(socket.AF_INET, socket.SOCK_STREAM)
		self.connect( (host, port) )
		self.buffer = "Test"

	def handle_connect(self):
		print "handle_connect"

	def handle_close(self):
		print "handle_close"
		self.close()

	def handle_read(self):
		print self.recv(8192)

	def handle_write(self):
		sent = self.send(self.buffer)
		self.buffer = self.buffer[sent:]

	def writable(self):
		return (len(self.buffer) > 0)

client = ClientTest('localhost', 25566)
asyncore.loop()