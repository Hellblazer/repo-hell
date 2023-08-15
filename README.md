# repo-hell
Hellblazing Repository
## Hellbound Maven Repository Configuration

    <repositories>
        <repository>
            <id>hell-repo</id>
            <url>https://raw.githubusercontent.com/Hellblazer/repo-hell/main/mvn-artifact</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>

    <pluginRepositories>
        <pluginRepository>
            <id>plugin-hell-repo</id>
            <url>https://raw.githubusercontent.com/Hellblazer/repo-hell/main/mvn-artifact</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </pluginRepository>
    </pluginRepositories>
