#include	"../ourhdr.h"
#include <unistd.h>         // for read/write
#include <sys/types.h>

int
main(void)
{
	printf("uid = %d, gid = %d\n", getuid(), getgid());
	exit(0);
}
