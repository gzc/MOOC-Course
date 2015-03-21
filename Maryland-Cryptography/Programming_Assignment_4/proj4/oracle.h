int macfd, vrfyfd;
int Oracle_Connect();
int Oracle_Disconnect();

int Mac(unsigned char *message, int mlength, unsigned char *tag);
int Vrfy(unsigned char *message, int mlength, unsigned char *tag);

