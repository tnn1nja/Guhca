cd target

del /Q "../../../Servers/guhcaDev/plugins\Guhca*"
for %%f in (Guhca*) do (
    copy "%%f" "../../../Servers/guhcaDev/plugins"
)

cd ..

echo File moved successfully.