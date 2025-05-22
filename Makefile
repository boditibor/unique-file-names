.PHONY: build run stop logs

build:
	podman run --rm --cap-add=SYS_PTRACE --security-opt=seccomp=unconfined \
	    -v $(CURDIR):/home/gradle/project:Z -v /tmp:/tmp:Z -w /home/gradle/project \
	    gradle:8.7-jdk21 /bin/bash -c "apt-get update && apt-get install -y procps && gradle generateJavadoc build"
	podman build -t unique-file-names-app .

run:
	podman compose up -d

stop:
	podman compose down

logs:
	podman compose logs -f
